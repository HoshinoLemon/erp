// SupplierDTO.java
package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class SupplierDTO implements Serializable {
    private Long id;             // 供应商ID（新增时为空）
    private String name;         // 名称
    private String contact;      // 联系人
    private String phone;        // 电话
    private String address;      // 地址
    private Integer status;      // 状态（0-禁用，1-启用）
}