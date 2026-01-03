package com.sky.dto;

import lombok.Data;

@Data // 关键：Lombok 的 @Data 注解会自动生成 getter/setter
public class PurchaseOrderQueryDTO {
    // 已有查询条件
    private String orderNumber;    // 订单编号（模糊查询）
    private Long supplierId;       // 供应商ID
    private Integer status;        // 订单状态

    // 分页参数（必须添加，PageHelper 会用到）
    private Integer page;          // 页码
    private Integer pageSize;      // 每页条数
    private Integer offset;        // 偏移量（分页插件自动计算）
    private Integer limit;         // 每页限制条数（分页插件自动计算）
}