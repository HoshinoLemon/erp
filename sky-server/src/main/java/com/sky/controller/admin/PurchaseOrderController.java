package com.sky.controller.admin;

import com.sky.dto.PurchaseOrderDTO;
import com.sky.dto.PurchaseOrderQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.PurchaseOrderService;
import com.sky.vo.PurchaseOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/purchase/order")
@Slf4j
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping
    public Result save(@RequestBody PurchaseOrderDTO orderDTO) {
        log.info("创建采购单：{}", orderDTO);
        purchaseOrderService.create(orderDTO);
        return Result.success();
    }

    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable Long id) {
        log.info("审核采购单：{}", id);
        purchaseOrderService.audit(id);
        return Result.success();
    }

    // 新增：分页查询采购订单
    @GetMapping("/page")
    public Result<PageResult> pageQuery(PurchaseOrderQueryDTO queryDTO) {
        PageResult pageResult = purchaseOrderService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    // 新增：根据ID查询采购订单
    @GetMapping("/{id}")
    public Result<PurchaseOrderVO> getById(@PathVariable Long id) {
        PurchaseOrderVO orderVO = purchaseOrderService.getById(id);
        return Result.success(orderVO);
    }

    // 完成采购单
    @PostMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        log.info("完成采购单：{}", id);
        purchaseOrderService.complete(id);
        return Result.success();
    }
}