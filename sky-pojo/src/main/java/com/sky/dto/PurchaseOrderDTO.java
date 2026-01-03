// PurchaseOrderDTO.java
package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PurchaseOrderDTO implements Serializable {
    private Long id;                     // 订单ID（新增时为空）
    private String orderNumber;          // 订单编号
    private Long supplierId;             // 供应商ID
    private BigDecimal totalAmount;      // 总金额
    private Integer status;              // 状态
    private List<PurchaseOrderItemDTO> items; // 采购明细
}