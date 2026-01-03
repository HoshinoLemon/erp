package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                 // 订单ID
    private String orderNumber;      // 订单编号
    private Long supplierId;         // 供应商ID
    private BigDecimal totalAmount;  // 总金额
    private Integer status;          // 状态（1-待审核，2-已审核，3-已完成，4-已取消）
    private LocalDateTime createTime;
    private LocalDateTime auditTime;
    private Long auditorId;          // 审核人ID
}