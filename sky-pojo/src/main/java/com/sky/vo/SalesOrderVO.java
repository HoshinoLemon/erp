// SalesOrderVO.java
package com.sky.vo;

import com.sky.vo.SalesOrderItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderVO {
    private Long id;
    private String orderNumber;
    private String customerName;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusStr;
    private LocalDateTime createTime;
    private LocalDateTime completionTime;
    private String operatorName;
    private List<SalesOrderItemVO> items;
}

