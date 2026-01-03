package com.sky.controller.admin;

import com.sky.dto.ProductDTO;
import com.sky.dto.ProductQueryDTO;
import com.sky.dto.StockAdjustDTO;
import com.sky.entity.Product;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 新增商品
     */
    @PostMapping
    public Result save(@RequestBody ProductDTO productDTO) {
        log.info("新增商品：{}", productDTO);
        productService.save(productDTO);
        return Result.success();
    }

    /**
     * 调整库存
     */
    @PostMapping("/stock/adjust")
    public Result adjustStock(@RequestBody StockAdjustDTO adjustDTO) {
        log.info("调整库存：{}", adjustDTO);
        productService.adjustStock(adjustDTO);
        return Result.success();
    }

    /**
     * 根据ID查询商品
     */
    @GetMapping("/{id}")
    public Result<ProductDTO> getById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getById(id);
        if (productDTO == null) {
            return Result.error("商品不存在");
        }
        return Result.success(productDTO);
    }

    /**
     * 新增：获取商品列表（供库存调整页面选择）
     */
    @GetMapping("/list")
    public Result<List<ProductDTO>> list() {
        List<ProductDTO> products = productService.list();
        return Result.success(products);
    }

    /**
     * 新增：修改商品
     */
    @PutMapping
    public Result update(@RequestBody ProductDTO product) {
        productService.update(product);
        return Result.success();
    }

    /**
     * 新增：删除商品
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String name,
            String code) {
        log.info("分页查询商品：页码={}, 每页条数={}, 名称={}, 编码={}", page, pageSize, name, code);
        // 封装查询条件
        ProductQueryDTO queryDTO = new ProductQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        queryDTO.setName(name);
        queryDTO.setCode(code);
        // 调用服务层查询
        PageResult pageResult = productService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }
}