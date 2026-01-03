import request from './request';

// 分页查询库存记录
export const getStockRecordPage = (params) => {
  return request.get('/admin/stockRecord/page', { params });
};