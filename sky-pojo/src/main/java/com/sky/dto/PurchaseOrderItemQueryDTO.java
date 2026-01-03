package com.sky.dto;

import lombok.Data;

@Data
public class PurchaseOrderItemQueryDTO {
    // 采购单ID（用于查询指定订单的明细）
    private Long purchaseOrderId;
    // 商品名称（模糊查询）
    private String productName;
    // 分页参数（由PageHelper自动拦截，无需手动处理）
    private Integer page;
    private Integer pageSize;
}