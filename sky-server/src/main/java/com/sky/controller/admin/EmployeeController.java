package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeeStatusDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工账号管理接口")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<Map<String, String>> login(@RequestBody EmployeeLoginDTO loginDTO) {
        log.info("员工登录：{}", loginDTO.getUsername());
        Employee employee = employeeService.login(loginDTO);

        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return Result.success(result);
    }

    /**
     * 新增员工
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO.getUsername());
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 更新员工状态
     */
    @PutMapping("/status")
    @ApiOperation("更新员工状态")
    public Result updateStatus(@RequestBody EmployeeStatusDTO statusDTO) {
        log.info("更新员工状态：{}，{}", statusDTO.getId(), statusDTO.getStatus());
        employeeService.updateStatus(statusDTO);
        return Result.success();
    }

    /**
     * 根据ID查询员工
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }
}