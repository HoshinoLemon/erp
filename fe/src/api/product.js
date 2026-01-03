import request from './request';

// 根据ID查询商品
export const getProductById = (id) => {
  return request.get(`/admin/product/${id}`);
};

// 调整商品库存
export const adjustStock = (data) => {
  return request.post('/admin/product/stock/adjust', data);
};

// 分页查询商品
export const getProductPage = (params) => {
  return request.get('/admin/product/page', { params });
};

// 新增商品
export const addProduct = (data) => {
  return request.post('/admin/product', data);
};

// 更新商品
export const updateProduct = (data) => {
  return request.put('/admin/product', data);
};