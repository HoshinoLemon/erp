package com.sky.dto;

import lombok.Data;

@Data
public class ProductQueryDTO {
    private int page; // 页码
    private int pageSize; // 每页条数
    private String name; // 商品名称
    private String code; // 商品编码
    private Long categoryId; // 分类ID
    private Integer status; // 状态
}