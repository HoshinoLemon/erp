package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class StockAdjustDTO implements Serializable {
    private Long productId;      // 商品ID
    private Integer quantity;    // 调整数量（正数增加，负数减少）
    private String remark;       // 原因
}