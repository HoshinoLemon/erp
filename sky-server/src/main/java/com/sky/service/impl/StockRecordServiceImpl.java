package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.StockAdjustDTO;
import com.sky.dto.StockRecordQueryDTO;
import com.sky.entity.StockRecord;
import com.sky.mapper.StockRecordMapper;
import com.sky.result.PageResult;
import com.sky.service.StockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockRecordServiceImpl implements StockRecordService {

    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    public PageResult pageQuery(StockRecordQueryDTO queryDTO) {
        // 开启分页
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        // 执行查询，PageHelper 会自动将结果封装为 Page 对象
        Page<StockRecord> page = (Page<StockRecord>) stockRecordMapper.pageQuery(queryDTO);
        // 构造并返回 PageResult
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void adjust(StockAdjustDTO adjustDTO) {

    }
}