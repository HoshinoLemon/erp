package com.sky.service;

import com.sky.dto.StockAdjustDTO;
import com.sky.dto.StockRecordQueryDTO;
import com.sky.result.PageResult;

public interface StockRecordService {
    PageResult pageQuery(StockRecordQueryDTO queryDTO);

    void adjust(StockAdjustDTO adjustDTO);
}
