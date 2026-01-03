// DailyStatisticVO.java
package com.sky.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DailyStatisticVO {
    private String date;      // 日期
    private BigDecimal amount;  // 金额
}