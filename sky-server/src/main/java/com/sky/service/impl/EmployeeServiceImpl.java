package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeeStatusDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import com.sky.utils.BCryptPasswordEncoder;
import com.sky.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     */
    @Override
    public Employee login(EmployeeLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 1. 根据用户名查询员工
        Employee employee = employeeMapper.getByUsername(username);
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 2. 校验密码（BCrypt加密比对）
        if (!BCryptPasswordEncoder.matches(password, employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 3. 校验账号状态
        if (employee.getStatus() == 0) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 4. 登录成功，返回员工信息
        return employee;
    }

    /**
     * 新增员工
     */
    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 拷贝属性
        BeanUtils.copyProperties(employeeDTO, employee);
        // 设置初始密码（123456，加密存储）
        employee.setPassword(BCryptPasswordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));
        // 设置状态（默认正常）
        employee.setStatus(1);
        // 设置时间和操作人（当前登录员工ID，需从ThreadLocal获取）
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(CurrentUserUtil.getCurrentUserId());
        employee.setUpdateUser(CurrentUserUtil.getCurrentUserId());

        employeeMapper.insert(employee);
    }

    /**
     * 更新员工状态
     */
    @Override
    @Transactional
    public void updateStatus(EmployeeStatusDTO statusDTO) {
        Employee employee = Employee.builder()
                .id(statusDTO.getId())
                .status(statusDTO.getStatus())
                .updateTime(LocalDateTime.now())
                .updateUser(CurrentUserUtil.getCurrentUserId())
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据ID查询员工
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }
}