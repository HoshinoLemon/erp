import request from './request';

// 获取供应商列表
export const getSupplierList = () => {
  return request.get('/admin/supplier/list');
};

// 根据ID获取供应商详情
export const getSupplierById = (id) => {
  return request.get(`/admin/supplier/${id}`);
};