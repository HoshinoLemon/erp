package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PurchaseOrderItemDTO implements Serializable {
    private Long productId;       // 商品ID
    private String productName;   // 商品名称
    private Integer quantity;     // 采购数量
    private BigDecimal unitPrice; // 单价
}