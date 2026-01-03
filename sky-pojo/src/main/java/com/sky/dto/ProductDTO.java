package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductDTO implements Serializable {
    private Long id;             // 新增时为空
    private String name;         // 商品名称
    private Long categoryId;     // 分类ID
    private String code;         // 商品编码
    private BigDecimal price;    // 售价
    private BigDecimal cost;     // 成本价
    private Integer stock;       // 初始库存
    private Integer status;      // 状态
    private String image;        // 图片
    private String description;  // 描述
}