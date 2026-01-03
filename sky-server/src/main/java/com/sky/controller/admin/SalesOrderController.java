package com.sky.controller.admin;

import com.sky.dto.SalesOrderDTO;
import com.sky.dto.SalesOrderQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SalesOrderService;
import com.sky.vo.SalesOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/sales/order")
@Slf4j
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    /**
     * 创建销售订单
     */
    @PostMapping
    public Result create(@RequestBody SalesOrderDTO orderDTO) {
        log.info("创建销售订单：{}", orderDTO);
        salesOrderService.create(orderDTO);
        return Result.success();
    }

    /**
     * 完成销售订单
     */
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        log.info("完成销售订单：{}", id);
        salesOrderService.complete(id);
        return Result.success();
    }

    /**
     * 分页查询销售订单
     */
    @GetMapping("/page")
    public Result<PageResult> page(SalesOrderQueryDTO queryDTO) {
        log.info("分页查询销售订单：{}", queryDTO);
        PageResult pageResult = salesOrderService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询销售订单
     */
    @GetMapping("/{id}")
    public Result<SalesOrderVO> getById(@PathVariable Long id) {
        log.info("根据ID查询销售订单：{}", id);
        SalesOrderVO salesOrderVO = salesOrderService.getById(id);
        return Result.success(salesOrderVO);
    }

    /**
     * 取消销售订单
     */
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        log.info("取消销售订单：{}", id);
        salesOrderService.cancel(id);
        return Result.success();
    }
}