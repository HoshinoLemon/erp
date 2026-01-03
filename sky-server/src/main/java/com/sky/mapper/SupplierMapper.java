package com.sky.mapper;

import com.sky.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SupplierMapper {

    /**
     * 新增供应商
     * @param supplier 供应商实体
     */
    void insert(Supplier supplier);

    /**
     * 根据ID查询供应商
     * @param id 供应商ID
     * @return 供应商实体
     */
    @Select("select * from supplier where id = #{id}")
    Supplier getById(Long id);

    /**
     * 更新供应商信息
     * @param supplier 供应商实体
     */
    @Update("update supplier set name = #{name}, contact = #{contact}, phone = #{phone}, " +
            "address = #{address}, status = #{status}, update_time = now() where id = #{id}")
    void update(Supplier supplier);

    /**
     * 根据ID删除供应商
     * @param id 供应商ID
     */
    @Update("delete from supplier where id = #{id}")
    void delete(Long id);

    @Select("select * from supplier")
    List<Supplier> list();
}