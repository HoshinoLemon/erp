package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "新增员工时传递的数据模型")
public class EmployeeDTO implements Serializable {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("角色")
    private String role; // ADMIN/EMPLOYEE
}