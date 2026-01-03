package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesStatisticVO {
    private BigDecimal totalSales;        // 总销售额
    private Integer orderCount;           // 订单总数
    private List<DailyStatisticVO> dailyList;  // 每日销售统计
}