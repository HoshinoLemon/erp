// SalesOrderDTO.java
package com.sky.dto;

import lombok.Data;
import java.util.List;

@Data
public class SalesOrderDTO {
    private String customerName;
    private List<SalesOrderItemDTO> items;
}