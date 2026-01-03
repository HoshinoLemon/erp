package com.sky.service.impl;

import com.sky.mapper.*;
import com.sky.service.StatisticService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;
    @Autowired
    private SalesOrderItemMapper salesOrderItemMapper;
    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Override
    public SalesStatisticVO salesStatistic(LocalDateTime start, LocalDateTime end) {
        // 查询销售总额
        BigDecimal totalSales = salesOrderMapper.sumAmountByTime(start, end);
        if (totalSales == null) {
            totalSales = BigDecimal.ZERO;
        }

        // 查询订单数量
        Integer orderCount = salesOrderMapper.countByTime(start, end);
        if (orderCount == null) {
            orderCount = 0;
        }

        // 按日统计销售额
        List<DailyStatisticVO> dailyList = salesOrderMapper.sumDailyAmount(start, end);

        return new SalesStatisticVO(totalSales, orderCount, dailyList);
    }

    @Override
    public PurchaseStatisticVO purchaseStatistic(LocalDateTime start, LocalDateTime end) {
        // 查询采购总额
        BigDecimal totalPurchase = (BigDecimal) purchaseOrderMapper.sumAmountByTime(start, end);
        if (totalPurchase == null) {
            totalPurchase = BigDecimal.ZERO;
        }

        // 查询采购单数量
        Integer orderCount = purchaseOrderMapper.countByTime(start, end);
        if (orderCount == null) {
            orderCount = 0;
        }

        // 按日统计采购额
        List<DailyStatisticVO> dailyList = purchaseOrderMapper.sumDailyAmount(start, end);

        // 按供应商统计采购额
        List<SupplierPurchaseVO> supplierList = purchaseOrderItemMapper.sumBySupplier(start, end);

        return new PurchaseStatisticVO(totalPurchase, orderCount, dailyList, supplierList);
    }

    @Override
    public StockStatisticVO stockStatistic() {
        // 查询总商品数
        Integer productCount = productMapper.countAll();

        // 查询总库存数量
        Integer totalStock = productMapper.sumTotalStock();

        // 查询库存预警商品数（库存低于阈值的商品）
        Integer warningCount = productMapper.countStockWarning();

        // 库存分布（按类别）
        List<CategoryStockVO> categoryStockList = productMapper.sumStockByCategory();

        return new StockStatisticVO(productCount, totalStock, warningCount, categoryStockList);
    }

    @Override
    public List<ProductRankVO> productSalesRank(LocalDateTime start, LocalDateTime end, int top) {
        // 查询商品销售统计
        List<ProductSalesVO> productSalesList = salesOrderItemMapper.sumByProduct(start, end);

        // 转换为商品销售排行VO
        List<ProductRankVO> result = new ArrayList<>();
        if (productSalesList != null) {
            for (int i = 0; i < productSalesList.size() && i < top; i++) {
                ProductSalesVO salesVO = productSalesList.get(i);
                ProductRankVO rankVO = new ProductRankVO();
                rankVO.setProductId(salesVO.getProductId());
                rankVO.setProductName(salesVO.getProductName());
                rankVO.setSalesVolume(salesVO.getTotalQuantity());
                rankVO.setSalesAmount(salesVO.getTotalAmount());
                rankVO.setRank(i + 1);
                result.add(rankVO);
            }
        }

        return result;
    }
}