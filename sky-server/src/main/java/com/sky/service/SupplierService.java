package com.sky.service;

import com.sky.dto.SupplierDTO;
import com.sky.entity.Supplier;

import java.util.List;


public interface SupplierService {
    void save(SupplierDTO supplierDTO);

    SupplierDTO getById(Long id);

    void update(SupplierDTO supplierDTO);

    void delete(Long id);

    List<Supplier> list();
}
