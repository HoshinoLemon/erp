package com.sky.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesOrderItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
