package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseStatisticVO {
    private BigDecimal totalPurchase;      // 总采购额
    private Integer orderCount;            // 采购单总数
    private List<DailyStatisticVO> dailyList;  // 每日采购统计
    private List<SupplierPurchaseVO> supplierList;  // 供应商采购统计
}