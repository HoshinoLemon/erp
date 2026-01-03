package com.sky.controller.admin;

import com.sky.service.StatisticService;
import com.sky.result.Result;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/statistic")
@Slf4j
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    /**
     * 销售统计
     */
    @GetMapping("/sales")
    public Result<SalesStatisticVO> salesStatistic(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        log.info("销售统计：{} 至 {}", start, end);
        SalesStatisticVO salesStatisticVO = statisticService.salesStatistic(start, end);
        return Result.success(salesStatisticVO);
    }

    /**
     * 采购统计
     */
    @GetMapping("/purchase")
    public Result<PurchaseStatisticVO> purchaseStatistic(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        log.info("采购统计：{} 至 {}", start, end);
        PurchaseStatisticVO purchaseStatisticVO = statisticService.purchaseStatistic(start, end);
        return Result.success(purchaseStatisticVO);
    }

    /**
     * 库存统计
     */
    @GetMapping("/stock")
    public Result<StockStatisticVO> stockStatistic() {
        log.info("库存统计");
        StockStatisticVO stockStatisticVO = statisticService.stockStatistic();
        return Result.success(stockStatisticVO);
    }

    /**
     * 商品销售排行
     */
    @GetMapping("/product/sales/rank")
    public Result<List<ProductRankVO>> productSalesRank(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(defaultValue = "10") int top) {
        log.info("商品销售排行：{} 至 {}，前{}名", start, end, top);
        List<ProductRankVO> productRankVOS = statisticService.productSalesRank(start, end, top);
        return Result.success(productRankVOS);
    }
}