package com.sky.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SupplierPurchaseVO {
    private Long supplierId;
    private String supplierName;
    private BigDecimal totalAmount;  // 采购总额
}