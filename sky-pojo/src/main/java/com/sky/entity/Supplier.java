package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;             // 供应商ID
    private String name;         // 名称
    private String contact;      // 联系人
    private String phone;        // 电话
    private String address;      // 地址
    private Integer status;      // 状态（0-禁用，1-启用）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}