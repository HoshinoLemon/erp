package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.ProductQueryDTO;
import com.sky.entity.Product;
import com.sky.vo.CategoryStockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper {

    void insert(Product product);

    @Select("select * from product where id = #{id}")
    Product getById(Long id);

    @Update("update product set stock = stock + #{quantity}, update_time = now() where id = #{productId}")
    void updateStock(Long productId, Integer quantity);

    /**
     * 查询商品总数
     * @return 商品总数
     */
    @Select("select count(*) from product")
    Integer countAll();

    /**
     * 查询总库存数量
     * @return 总库存数量
     */
    @Select("select sum(stock) from product")
    Integer sumTotalStock();

    /**
     * 查询库存预警商品数
     * @return 库存预警商品数
     */
    @Select("select count(*) from product where stock <= 10")  // 假设阈值为10
    Integer countStockWarning();

    /**
     * 按类别统计库存
     * @return 类别库存统计
     */
    List<CategoryStockVO> sumStockByCategory();

    /**
     * 查询所有商品
     * @return 商品列表
     */
    List<Product> listAll();

    /**
     * 更新商品信息
     * @param product 商品实体
     */
    void update(Product product);

    /**
     * 删除商品
     * @param id 商品ID
     */
    void delete(Long id);

    /**
     * 分页查询商品
     * @param queryDTO 查询条件
     * @return 商品分页列表
     */
    Page<Product> pageQuery(ProductQueryDTO queryDTO);
}