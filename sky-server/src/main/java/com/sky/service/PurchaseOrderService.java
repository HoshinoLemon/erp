package com.sky.service;

import com.sky.dto.PurchaseOrderDTO;
import com.sky.dto.PurchaseOrderQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.PurchaseOrderVO;

public interface PurchaseOrderService {
    void create(PurchaseOrderDTO orderDTO);
    void audit(Long id);
    PageResult pageQuery(PurchaseOrderQueryDTO queryDTO); // 新增分页查询方法
    PurchaseOrderVO getById(Long id); // 新增根据ID查询方法
    void complete(Long id);
}
