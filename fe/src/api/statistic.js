import request from './request';

// 格式化时间为 "yyyy-MM-dd HH:mm:ss"
const formatDateTime = (date) => {
  // 确保date是Date对象（添加容错处理）
  if (!(date instanceof Date)) {
    date = new Date(date);
  }
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};
// 销售统计
export const getSalesStatistic = (params) => {
  const formattedParams = {
    ...params,
    start: params.start ? formatDateTime(params.start) : '', // 传入Date对象
    end: params.end ? formatDateTime(params.end) : ''
  };
  return request.get('/admin/statistic/sales', { params: formattedParams });
};

// 采购统计
export const getPurchaseStatistic = (params) => {
  const formattedParams = {
    ...params,
    start: params.start ? formatDateTime(params.start) : '',
    end: params.end ? formatDateTime(params.end) : ''
  };
  return request.get('/admin/statistic/purchase', { params: formattedParams });
};
// 库存统计
export const getStockStatistic = () => {
  return request.get('/admin/statistic/stock');
};

// 商品销售排行
export const getProductSalesRank = (params) => {
  const formattedParams = {
    ...params,
    start: params.start ? formatDateTime(params.start) : '',
    end: params.end ? formatDateTime(params.end) : '',
    top: params.top || 10
  };
  return request.get('/admin/statistic/product/rank', { params: formattedParams });
};