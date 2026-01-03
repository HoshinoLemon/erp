package com.sky.service.impl;

import com.sky.dto.SupplierDTO;
import com.sky.entity.Supplier;
import com.sky.mapper.SupplierMapper;
import com.sky.service.SupplierService;
import com.sky.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public void save(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDTO, supplier);
        // 处理状态为空的情况，设置默认值为1（启用）
        if (supplier.getStatus() == null) {
            supplier.setStatus(1);
        }
        supplier.setCreateTime(LocalDateTime.now());
        supplier.setUpdateTime(LocalDateTime.now());
        supplierMapper.insert(supplier);
    }

    @Override
    public SupplierDTO getById(Long id) {
        Supplier supplier = supplierMapper.getById(id);
        SupplierDTO dto = new SupplierDTO();
        BeanUtils.copyProperties(supplier, dto);
        return dto;
    }

    @Override
    public void update(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDTO, supplier);
        supplier.setUpdateTime(LocalDateTime.now());
        supplierMapper.update(supplier);
    }

    @Override
    public void delete(Long id) {
        supplierMapper.delete(id);
    }

    @Override
    public List<Supplier> list() {
        return supplierMapper.list();
    }
}