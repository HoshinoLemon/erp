package com.sky.service;

import com.sky.dto.ProductDTO;
import com.sky.dto.ProductQueryDTO;
import com.sky.dto.StockAdjustDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface ProductService {
    void save(ProductDTO productDTO);
    void adjustStock(StockAdjustDTO adjustDTO);
    ProductDTO getById(Long id);
    List<ProductDTO> list(); // 新增查询所有商品
    void update(ProductDTO productDTO); // 新增更新商品
    void delete(Long id); // 新增删除商品
    PageResult pageQuery(ProductQueryDTO queryDTO); // 新增分页查询
}
