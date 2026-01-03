package com.sky.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSalesVO {
    private Long productId;
    private String productName;
    private Integer totalQuantity;  // 总销量
    private BigDecimal totalAmount;  // 总销售额
}