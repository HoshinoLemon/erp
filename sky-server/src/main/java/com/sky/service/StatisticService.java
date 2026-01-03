package com.sky.service;

import com.sky.vo.ProductRankVO;
import com.sky.vo.PurchaseStatisticVO;
import com.sky.vo.SalesStatisticVO;
import com.sky.vo.StockStatisticVO;

import java.time.LocalDateTime;
import java.util.List;

// StatisticService.java
public interface StatisticService {
    // 销售统计
    SalesStatisticVO salesStatistic(LocalDateTime start, LocalDateTime end);

    // 采购统计
    PurchaseStatisticVO purchaseStatistic(LocalDateTime start, LocalDateTime end);

    // 库存统计
    StockStatisticVO stockStatistic();

    // 商品销售排行
    List<ProductRankVO> productSalesRank(LocalDateTime start, LocalDateTime end, int top);
}