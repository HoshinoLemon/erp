import request from './request';

// 管理员登录
export const login = (username, password) => {
  return request.post('/admin/employee/login', { username, password });
};

// 获取当前用户信息（示例）
export const getCurrentUser = () => {
  return request.get('/admin/employee/current');
};