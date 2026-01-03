package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购单明细表实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                 // 明细ID
    private Long orderId;            // 采购单ID
    private Long productId;          // 商品ID
    private String productName;      // 商品名称
    private Integer quantity;        // 采购数量
    private BigDecimal unitPrice;    // 单价
    private BigDecimal totalPrice;   // 总价（数量×单价）
}