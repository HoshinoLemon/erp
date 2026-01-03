package com.sky.vo;

import com.sky.entity.PurchaseOrderItem;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderVO {
    private Long id; // 订单ID
    private String orderNumber; // 订单编号
    private Long supplierId; // 供应商ID
    private String supplierName; // 供应商名称
    private BigDecimal totalAmount; // 总金额
    private Integer status; // 状态
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime auditTime; // 审核时间
    private String auditorName; // 审核人名称
    private List<PurchaseOrderItem> items; // 订单明细
}