package com.sky.service;

import com.sky.dto.SalesOrderDTO;
import com.sky.dto.SalesOrderQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SalesOrderVO;

public interface SalesOrderService {
    void create(SalesOrderDTO orderDTO);
    void complete(Long id);
    PageResult pageQuery(SalesOrderQueryDTO queryDTO);
    SalesOrderVO getById(Long id);
    void cancel(Long id); // 新增取消订单方法
}