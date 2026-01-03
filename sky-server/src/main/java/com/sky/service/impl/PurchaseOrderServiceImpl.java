package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.sky.dto.PurchaseOrderDTO;
import com.sky.dto.PurchaseOrderItemDTO;
import com.sky.dto.PurchaseOrderQueryDTO;
import com.sky.entity.*;
import com.sky.exception.BusinessException;
import com.sky.exception.NotFoundException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.PurchaseOrderService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.PurchaseOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper orderMapper;
    @Autowired
    private PurchaseOrderItemMapper itemMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    @Transactional
    public void create(PurchaseOrderDTO orderDTO) {
        // 生成订单编号
        String orderNumber = "PO" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);

        // 保存采购单
        PurchaseOrder order = new PurchaseOrder();
        BeanUtils.copyProperties(orderDTO, order);
        order.setOrderNumber(orderNumber);
        order.setStatus(1); // 待审核
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);

        // 保存采购明细
        for (PurchaseOrderItemDTO itemDTO : orderDTO.getItems()) {
            PurchaseOrderItem item = new PurchaseOrderItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setOrderId(order.getId());
            item.setTotalPrice(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            itemMapper.insert(item);
        }
    }

    @Override
    @Transactional
    public void audit(Long id) {
        // 校验订单ID非空
        if (id == null) {
            throw new IllegalArgumentException("订单ID不能为空");
        }

        // 校验订单存在
        PurchaseOrder order = orderMapper.getById(id);
        if (order == null) {
            throw new NotFoundException("采购订单不存在");
        }

        // 校验订单状态合法
        if (order.getStatus() != 1) { // 是待审核状态
            throw new BusinessException("当前订单状态不允许审核");
        }

        order.setStatus(2); // 已审核
        order.setAuditTime(LocalDateTime.now());
        order.setAuditorId(CurrentUserUtil.getCurrentUserId());
        orderMapper.update(order);
    }

    @Override
    public PageResult pageQuery(PurchaseOrderQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<PurchaseOrderVO> page = orderMapper.pageQuery(queryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public PurchaseOrderVO getById(Long id) {
        // 查询订单基本信息
        PurchaseOrder order = orderMapper.getById(id);
        if (order == null) {
            throw new NotFoundException("采购订单不存在");
        }

        // 转换为VO
        PurchaseOrderVO vo = new PurchaseOrderVO();
        BeanUtils.copyProperties(order, vo);

        // 查询订单明细
        List<PurchaseOrderItem> items = itemMapper.listByOrderId(id);
        vo.setItems(items);

//         查询供应商名称
         Supplier supplier = supplierMapper.getById(order.getSupplierId());
         if (supplier != null) {
             vo.setSupplierName(supplier.getName());
         }

        return vo;
    }

    @Override
    @Transactional
    public void complete(Long id) {
        // 校验订单ID非空
        if (id == null) {
            throw new IllegalArgumentException("订单ID不能为空");
        }

        // 校验订单存在
        PurchaseOrder order = orderMapper.getById(id);
        if (order == null) {
            throw new NotFoundException("采购订单不存在");
        }

        // 校验订单状态合法
        if (order.getStatus() != 2) { // 已审核
            throw new BusinessException("当前订单未审核");
        }

        order.setStatus(3); // 已完成
        order.setAuditTime(LocalDateTime.now());
        order.setAuditorId(CurrentUserUtil.getCurrentUserId());
        orderMapper.update(order);

        // 批量处理订单明细
        List<PurchaseOrderItem> items = itemMapper.listByOrderId(id);
        if (CollectionUtils.isEmpty(items)) {
            throw new BusinessException("订单明细不能为空");
        }

        for (PurchaseOrderItem item : items) {
            // 校验明细数据
            if (item.getProductId() == null) {
                throw new BusinessException("商品ID不能为空");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException("采购数量必须大于0");
            }

            productMapper.updateStock(item.getProductId(), item.getQuantity());
            // 记录库存变动
            Product product = productMapper.getById(item.getProductId());

            StockRecord record = StockRecord.builder()
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .quantity(item.getQuantity())
                    .type(1)
                    .remark("采购入库（订单号：" + order.getOrderNumber() + "）")
                    .operateTime(LocalDateTime.now())
                    .operatorId(CurrentUserUtil.getCurrentUserId())
                    .TotalAfterAlter((long) (product.getStock()))
                    .build();
            stockRecordMapper.insert(record);
        }
    }
}