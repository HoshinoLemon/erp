package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SalesOrderQueryDTO;
import com.sky.entity.SalesOrder;
import com.sky.vo.DailyStatisticVO;
import com.sky.vo.SalesOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SalesOrderMapper {

    /**
     * 新增销售订单
     * @param salesOrder 销售订单实体
     */
    void insert(SalesOrder salesOrder);

    /**
     * 根据ID查询销售订单
     * @param id 订单ID
     * @return 销售订单实体
     */
    @Select("select * from sales_order where id = #{id}")
    SalesOrder getById(Long id);

    /**
     * 更新销售订单
     * @param salesOrder 销售订单实体
     */
    void update(SalesOrder salesOrder);

    /**
     * 分页查询销售订单
     * @param queryDTO 查询条件
     * @return 销售订单列表
     */
    Page<SalesOrderVO> pageQuery(SalesOrderQueryDTO queryDTO);

    /**
     * 查询符合条件的订单总数
     * @param queryDTO 查询条件
     * @return 订单总数
     */
    Integer count(SalesOrderQueryDTO queryDTO);

    /**
     * 按时间统计销售总额
     * @param start 开始时间
     * @param end 结束时间
     * @return 销售总额
     */
    @Select("select sum(total_amount) from sales_order where create_time between #{start} and #{end} and status = 2")
    BigDecimal sumAmountByTime(LocalDateTime start, LocalDateTime end);

    /**
     * 按时间统计订单数量
     * @param start 开始时间
     * @param end 结束时间
     * @return 订单数量
     */
    @Select("select count(*) from sales_order where create_time between #{start} and #{end} and status = 2")
    Integer countByTime(LocalDateTime start, LocalDateTime end);

    /**
     * 按日统计销售额
     * @param start 开始时间
     * @param end 结束时间
     * @return 每日销售额统计
     */
    List<DailyStatisticVO> sumDailyAmount(LocalDateTime start, LocalDateTime end);
}