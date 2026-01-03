package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeeStatusDTO;
import com.sky.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO loginDTO);

    /**
     * 新增员工
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 更新员工状态（启用/禁用）
     */
    void updateStatus(EmployeeStatusDTO statusDTO);

    /**
     * 根据ID查询员工
     */
    Employee getById(Long id);
}