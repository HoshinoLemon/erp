package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username 用户名
     * @return 员工实体
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee 员工实体
     */
    void insert(Employee employee);

    /**
     * 根据ID查询员工
     * @param id 员工ID
     * @return 员工实体
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 更新员工信息
     * @param employee 员工实体
     */
    @Update("update employee set username = #{username}, name = #{name}, phone = #{phone}, " +
            "status = #{status}, role = #{role}, update_time = #{updateTime}, " +
            "update_user = #{updateUser} where id = #{id}")
    void update(Employee employee);
}