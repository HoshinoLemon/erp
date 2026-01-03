package com.sky.mapper;

import com.sky.entity.SalesOrderItem;
import com.sky.vo.ProductSalesVO;
import com.sky.vo.SupplierPurchaseVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SalesOrderItemMapper {

    /**
     * 新增销售订单明细
     * @param salesOrderItem 销售订单明细实体
     */
    void insert(SalesOrderItem salesOrderItem);

    /**
     * 批量插入销售订单明细
     * @param items 销售订单明细列表
     */
    void batchInsert(List<SalesOrderItem> items);

    /**
     * 根据订单ID查询明细
     * @param orderId 订单ID
     * @return 明细列表
     */
    List<SalesOrderItem> listByOrderId(Long orderId);

    /**
     * 按商品统计销售数量和金额
     * @param start 开始时间
     * @param end 结束时间
     * @return 商品销售统计
     */
    List<ProductSalesVO> sumByProduct(LocalDateTime start, LocalDateTime end);
}