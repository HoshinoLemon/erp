package com.sky.controller.admin;

import com.sky.dto.StockAdjustDTO;
import com.sky.dto.StockRecordQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.StockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// 新增StockController的调整方法校验
@RestController
@RequestMapping("/admin/stockRecord")
public class StockRecordController {

    @Autowired
    private StockRecordService stockRecordService;

    @PostMapping("/adjust")
    public Result adjustStock(@RequestBody StockAdjustDTO adjustDTO) {
        if (adjustDTO == null) {
            return Result.error("调整信息不能为空");
        }
        if (adjustDTO.getProductId() == null) {
            return Result.error("商品ID不能为空");
        }
        if (adjustDTO.getQuantity() == null) {
            return Result.error("调整数量不能为空");
        }
        // 禁止零库存调整
        if (adjustDTO.getQuantity() == 0) {
            return Result.error("调整数量不能为0");
        }

        stockRecordService.adjust(adjustDTO);
        return Result.success();
    }

    // 新增：分页查询库存记录
    @GetMapping("/page")
    public Result<PageResult> pageQuery(StockRecordQueryDTO queryDTO) {
        PageResult pageResult = stockRecordService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }
}