package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;             // 商品ID
    private String name;         // 商品名称
    private Long categoryId;     // 分类ID
    private String code;         // 商品编码
    private BigDecimal price;    // 售价
    private BigDecimal cost;     // 成本价
    private Integer stock;       // 库存数量
    private Integer status;      // 状态（0-下架，1-上架）
    private String image;        // 商品图片
    private String description;  // 商品描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}