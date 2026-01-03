// 分页查询采购订单
import request from './request'; // 添加这行导入
export const getPurchaseOrderPage = (params) => {
  return request.get('/admin/purchase/order/page', { params });
};

// 审核采购订单
export const auditPurchaseOrder = (id) => {
  return request.put(`/admin/purchase/order/audit/${id}`);
};

// 完成采购订单
export const completePurchaseOrder = (id) => {
  return request.post(`/admin/purchase/order/complete/${id}`);
};

// 获取供应商列表
export const getSupplierList = () => {
  return request.get('/admin/supplier/list');
};