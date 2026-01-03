package com.sky.vo;

import lombok.Data;

@Data
public class CategoryStockVO {
    private Long categoryId;
    private String categoryName;
    private Integer stockQuantity;  // 库存数量
}