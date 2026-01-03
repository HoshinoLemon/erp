package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                 // 记录ID
    private Long productId;          // 商品ID
    private String productName;      // 商品名称
    private Integer quantity;        // 变动数量（正数入库，负数出库）
    private Integer type;            // 类型（1-采购入库，2-销售出库，3-盘点调整）
    private String remark;           // 备注
    private LocalDateTime operateTime;
    private Long operatorId;         // 操作人ID
    private Long TotalAfterAlter;   // 变更后总数
}