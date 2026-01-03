package com.sky.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRankVO {
    private Long productId;
    private String productName;
    private Integer salesVolume;  // 销售量
    private BigDecimal salesAmount;  // 销售额
    private Integer rank;  // 排名
}