package com.sky.controller.admin;

import com.alibaba.druid.util.StringUtils;
import com.sky.dto.SupplierDTO;
import com.sky.entity.Supplier;
import com.sky.result.Result;
import com.sky.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/supplier")
@Slf4j
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public Result save(@RequestBody SupplierDTO supplierDTO) {
        log.info("新增供应商：{}", supplierDTO);
        supplierService.save(supplierDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SupplierDTO> getById(@PathVariable Long id) {
        return Result.success(supplierService.getById(id));
    }

    @PutMapping
    public Result update(@RequestBody SupplierDTO supplierDTO) {
        // 基础参数校验
        if (supplierDTO == null) {
            return Result.error("供应商信息不能为空");
        }
        if (supplierDTO.getId() == null) {
            return Result.error("供应商ID不能为空");
        }
        if (StringUtils.isEmpty(supplierDTO.getName())) {
            return Result.error("供应商名称不能为空");
        }

        log.info("修改供应商：{}", supplierDTO);
        supplierService.update(supplierDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Supplier>> list() {
        List<Supplier> suppliers = supplierService.list();
        return Result.success(suppliers);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除供应商：{}", id);
        supplierService.delete(id);
        return Result.success();
    }
}