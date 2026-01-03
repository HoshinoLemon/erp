package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// SalesOrder.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderNumber;
    private String customerName;
    private BigDecimal totalAmount;
    private Integer status; // 1-待发货，2-已完成，3-已取消
    private LocalDateTime createTime;
    private LocalDateTime completionTime;
    private Long operatorId;
}