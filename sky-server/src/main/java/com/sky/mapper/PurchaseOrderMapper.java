package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.PurchaseOrderQueryDTO;
import com.sky.entity.PurchaseOrder;
import com.sky.vo.DailyStatisticVO;
import com.sky.vo.PurchaseOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PurchaseOrderMapper<BigDecimal> {

    /**
     * 新增采购单
     * @param purchaseOrder 采购单实体
     */
    void insert(PurchaseOrder purchaseOrder);

    /**
     * 根据ID查询采购单
     * @param id 采购单ID
     * @return 采购单实体
     */
    @Select("select * from purchase_order where id = #{id}")
    PurchaseOrder getById(Long id);

    /**
     * 更新采购单
     * @param purchaseOrder 采购单实体
     */
    @Update("update purchase_order set status = #{status}, audit_time = #{auditTime}, auditor_id = #{auditorId}, " +
            "update_time = now() where id = #{id}")
    void update(PurchaseOrder purchaseOrder);

    /**
     * 按时间统计采购总额
     * @param start 开始时间
     * @param end 结束时间
     * @return 采购总额
     */
    @Select("select sum(total_amount) from purchase_order where create_time between #{start} and #{end} and status = 2")
    BigDecimal sumAmountByTime(LocalDateTime start, LocalDateTime end);

    /**
     * 按时间统计采购单数量
     * @param start 开始时间
     * @param end 结束时间
     * @return 采购单数量
     */
    @Select("select count(*) from purchase_order where create_time between #{start} and #{end} and status = 2")
    Integer countByTime(LocalDateTime start, LocalDateTime end);

    /**
     * 按日统计采购额
     * @param start 开始时间
     * @param end 结束时间
     * @return 每日采购额统计
     */
    List<DailyStatisticVO> sumDailyAmount(LocalDateTime start, LocalDateTime end);

    /**
     * 分页查询采购订单
     * @param queryDTO 查询条件
     * @return 采购订单VO分页列表
     */
    Page<PurchaseOrderVO> pageQuery(PurchaseOrderQueryDTO queryDTO);
}