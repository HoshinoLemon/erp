package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存记录查询DTO
 */
@Data
public class StockRecordQueryDTO implements Serializable {
    private Long productId;      // 商品ID
    private Integer type;        // 记录类型（1-采购入库，2-销售出库，3-盘点调整）
    private LocalDateTime beginTime;  // 开始时间
    private LocalDateTime endTime;    // 结束时间
    private int page;            // 页码
    private int pageSize;        // 每页记录数
}