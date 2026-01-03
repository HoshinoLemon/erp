package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.ProductDTO;
import com.sky.dto.ProductQueryDTO;
import com.sky.dto.StockAdjustDTO;
import com.sky.entity.Product;
import com.sky.entity.StockRecord;
import com.sky.exception.BusinessException;
import com.sky.exception.NotFoundException;
import com.sky.mapper.ProductMapper;
import com.sky.mapper.StockRecordMapper;
import com.sky.result.PageResult;
import com.sky.service.ProductService;
import com.sky.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    @Transactional
    public void save(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setCreateTime(java.time.LocalDateTime.now());
        product.setUpdateTime(java.time.LocalDateTime.now());
        product.setCreateUser(CurrentUserUtil.getCurrentUserId());
        product.setUpdateUser(CurrentUserUtil.getCurrentUserId());
        productMapper.insert(product);

        // 初始化库存记录
        if (productDTO.getStock() != null && productDTO.getStock() > 0) {
            StockRecord record = StockRecord.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(productDTO.getStock())
                    .type(1) // 采购入库
                    .remark("初始化入库")
                    .operateTime(java.time.LocalDateTime.now())
                    .operatorId(CurrentUserUtil.getCurrentUserId())
                    .TotalAfterAlter(Long.valueOf(product.getStock()))
                    .build();
            stockRecordMapper.insert(record);
        }
    }

    @Override
    @Transactional
    public void adjustStock(StockAdjustDTO adjustDTO) {
        // 校验库存是否充足（当调整数量为负数时）
        if (adjustDTO.getQuantity() < 0) {
            Product product = productMapper.getById(adjustDTO.getProductId());
            if (product == null) {
                throw new NotFoundException("商品不存在");
            }
            if (product.getStock() + adjustDTO.getQuantity() < 0) { // 库存+调整量（负数）<0即不足
                throw new BusinessException("库存不足，当前库存：" + product.getStock());
            }
        }

        // 更新商品库存
        productMapper.updateStock(adjustDTO.getProductId(), adjustDTO.getQuantity());

        // 记录库存变动
        Product product = productMapper.getById(adjustDTO.getProductId());
        StockRecord record = StockRecord.builder()
                .productId(product.getId())
                .productName(product.getName())
                .quantity(adjustDTO.getQuantity())
                .type(3) // 盘点调整
                .remark(adjustDTO.getRemark())
                .operateTime(java.time.LocalDateTime.now())
                .operatorId(CurrentUserUtil.getCurrentUserId())
                .TotalAfterAlter(Long.valueOf(product.getStock()))
                .build();
        stockRecordMapper.insert(record);
    }

    @Override
    public ProductDTO getById(Long id) {
        Product product = productMapper.getById(id);
        if (product == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    @Override
    public List<ProductDTO> list() {
        List<Product> products = productMapper.listAll();
        return products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            BeanUtils.copyProperties(product, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void update(ProductDTO productDTO) {
        if (productDTO.getId() == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setUpdateTime(LocalDateTime.now());
        product.setUpdateUser(CurrentUserUtil.getCurrentUserId());
        productMapper.update(product);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        productMapper.delete(id);
    }

    @Override
    public PageResult pageQuery(ProductQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<Product> page = productMapper.pageQuery(queryDTO);
        List<ProductDTO> dtoList = page.getResult().stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            BeanUtils.copyProperties(product, dto);
            return dto;
        }).collect(Collectors.toList());
        return new PageResult(page.getTotal(), dtoList);
    }
}