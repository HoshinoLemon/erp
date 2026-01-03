package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.*;
import com.sky.entity.Product;
import com.sky.entity.SalesOrder;
import com.sky.entity.SalesOrderItem;
import com.sky.entity.StockRecord;
import com.sky.exception.BusinessException;
import com.sky.exception.NotFoundException;
import com.sky.mapper.ProductMapper;
import com.sky.mapper.SalesOrderItemMapper;
import com.sky.mapper.SalesOrderMapper;
import com.sky.mapper.StockRecordMapper;
import com.sky.result.PageResult;
import com.sky.service.ProductService;
import com.sky.service.SalesOrderService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.SalesOrderItemVO;
import com.sky.vo.SalesOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SalesOrderServiceImpl implements SalesOrderService {
    @Autowired
    private SalesOrderMapper orderMapper;
    @Autowired
    private SalesOrderItemMapper itemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    @Transactional
    public void create(SalesOrderDTO orderDTO) {
        // 生成订单编号
        String orderNumber = "SO" + System.currentTimeMillis() +
                UUID.randomUUID().toString().substring(0, 6);

        // 先计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SalesOrderItemDTO itemDTO : orderDTO.getItems()) {
            // 计算明细金额并累加总金额
            BigDecimal itemTotal = itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // 保存销售订单（此时已设置总金额）
        SalesOrder order = new SalesOrder();
        BeanUtils.copyProperties(orderDTO, order);
        order.setOrderNumber(orderNumber);
        order.setStatus(1); // 待发货
        order.setCreateTime(LocalDateTime.now());
        order.setOperatorId(CurrentUserUtil.getCurrentUserId());
        order.setTotalAmount(totalAmount); // 关键：插入前设置总金额
        orderMapper.insert(order);

        // 保存销售明细
        for (SalesOrderItemDTO itemDTO : orderDTO.getItems()) {
            SalesOrderItem item = new SalesOrderItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setOrderId(order.getId());
            item.setTotalPrice(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            itemMapper.insert(item);
        }

        // 移除后续更新总金额的代码（已不需要）
        // order.setTotalAmount(totalAmount);
        // orderMapper.update(order);
    }

    @Override
    @Transactional
    public void complete(Long id) {
        // 实现订单完成逻辑，包括扣减库存等
        SalesOrder order = orderMapper.getById(id);
        if (order == null) {
            throw new NotFoundException("销售订单不存在");
        }

        if (order.getStatus() != 1) {
            throw new BusinessException("当前订单状态不允许完成");
        }

        // 处理订单明细，扣减库存
        List<SalesOrderItem> items = itemMapper.listByOrderId(id);
        for (SalesOrderItem item : items) {
            // 扣减库存
            productMapper.updateStock(item.getProductId(), -item.getQuantity());
            Product product = productMapper.getById(item.getProductId());

            // 记录库存变动
            StockRecord record = StockRecord.builder()
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .quantity(-item.getQuantity()) // 负数表示出库
                    .type(2) // 销售出库
                    .remark("销售出库（订单号：" + order.getOrderNumber() + "）")
                    .operateTime(LocalDateTime.now())
                    .operatorId(CurrentUserUtil.getCurrentUserId())
                    .TotalAfterAlter(Long.valueOf(product.getStock()))
                    .build();
            stockRecordMapper.insert(record);
        }

        // 更新订单状态
        order.setStatus(2); // 已完成
        order.setCompletionTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public PageResult pageQuery(SalesOrderQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<SalesOrderVO> page = orderMapper.pageQuery(queryDTO);

        List<SalesOrderVO> voList = page.getResult().stream().map(order -> {
            SalesOrderVO vo = new SalesOrderVO();
            BeanUtils.copyProperties(order, vo);
            // 查询订单明细
            List<SalesOrderItem> items = itemMapper.listByOrderId(order.getId());
            List<SalesOrderItemVO> itemVos = new ArrayList<>();
            for (SalesOrderItem item : items) {
                SalesOrderItemVO itemVo = new SalesOrderItemVO();
                BeanUtils.copyProperties(item, itemVo);
                itemVos.add(itemVo);
            }
            vo.setItems(itemVos);
            return vo;
        }).collect(Collectors.toList());

        return new PageResult(page.getTotal(), voList);
    }

    @Override
    public SalesOrderVO getById(Long id) {
        SalesOrder order = orderMapper.getById(id);
        if (order == null) {
            throw new NotFoundException("销售订单不存在");
        }

        SalesOrderVO vo = new SalesOrderVO();
        BeanUtils.copyProperties(order, vo);
        List<SalesOrderItem> items = itemMapper.listByOrderId(id);
        List<SalesOrderItemVO> itemVos = new ArrayList<>();
        for (SalesOrderItem item : items) {
            SalesOrderItemVO itemVo = new SalesOrderItemVO();
            BeanUtils.copyProperties(item, itemVo);
            itemVos.add(itemVo);
        }
        vo.setItems(itemVos);
        return vo;
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        SalesOrder order = orderMapper.getById(id);
        if (order == null) {
            throw new NotFoundException("销售订单不存在");
        }

        if (order.getStatus() != 1) {
            throw new BusinessException("当前订单状态不允许取消");
        }

        // 更新订单状态为已取消
        order.setStatus(3); // 3-已取消
        orderMapper.update(order);
    }
}