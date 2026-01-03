// SalesOrderQueryDTO.java
package com.sky.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SalesOrderQueryDTO {
    private int page;
    private int pageSize;
    private String orderNumber;
    private String customerName;
    private Integer status;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    // 计算分页起始位置
    public int getStartIndex() {
        return (page - 1) * pageSize;
    }
}