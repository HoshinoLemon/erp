import request from './request';

// 创建销售订单
export const createSalesOrder = (data) => {
  return request.post('/admin/sales/order', data);
};

// 完成销售订单
export const completeSalesOrder = (id) => {
  return request.put(`/admin/sales/order/complete/${id}`);
};

// 分页查询销售订单
export const getSalesOrderPage = (params) => {
  return request.get('/admin/sales/order/page', { params });
};

// 根据ID查询销售订单
export const getSalesOrderById = (id) => {
  return request.get(`/admin/sales/order/${id}`);
};