// SalesOrderItemVO.java
package com.sky.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesOrderItemVO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}