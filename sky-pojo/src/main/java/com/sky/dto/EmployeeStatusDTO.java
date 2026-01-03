package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "更新员工状态的数据模型")
public class EmployeeStatusDTO implements Serializable {
    @ApiModelProperty("员工ID")
    private Long id;
    @ApiModelProperty("状态：0-禁用，1-正常")
    private Integer status;
}