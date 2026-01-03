package com.sky.mapper;

import com.sky.dto.StockRecordQueryDTO;
import com.sky.entity.StockRecord;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockRecordMapper {

    /**
     * 插入库存记录
     * @param stockRecord 库存记录实体
     */
    void insert(StockRecord stockRecord);

    List<StockRecord> pageQuery(StockRecordQueryDTO queryDTO);
}