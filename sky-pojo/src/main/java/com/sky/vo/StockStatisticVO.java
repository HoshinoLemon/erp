package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockStatisticVO {
    private Integer productCount;          // 商品总数
    private Integer totalStock;            // 总库存数量
    private Integer warningCount;          // 库存预警商品数
    private List<CategoryStockVO> categoryStockList;  // 类别库存统计
}