package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.PurchaseOrderItemQueryDTO;
import com.sky.dto.PurchaseOrderQueryDTO;
import com.sky.entity.PurchaseOrder;
import com.sky.entity.PurchaseOrderItem;
import com.sky.vo.SupplierPurchaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PurchaseOrderItemMapper {

    void insert(PurchaseOrderItem purchaseOrderItem);

    /**
     * 批量插入采购单明细
     * @param items 采购单明细列表
     */
    void batchInsert(List<PurchaseOrderItem> items);

    /**
     * 根据采购单ID查询明细
     * @param orderId 采购单ID
     * @return 明细列表
     */
    List<PurchaseOrderItem> listByOrderId(Long orderId);

    // 在PurchaseOrderItemMapper接口中添加以下方法
    /**
     * 按供应商统计采购额
     * @param start 开始时间
     * @param end 结束时间
     * @return 供应商采购统计
     */
    List<SupplierPurchaseVO> sumBySupplier(LocalDateTime start, LocalDateTime end);

    Page<PurchaseOrderItem> pageQuery(PurchaseOrderItemQueryDTO queryDTO);
}