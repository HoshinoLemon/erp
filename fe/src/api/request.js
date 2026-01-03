import axios from 'axios';
import router from '../router';

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000
});

// 请求拦截器：添加令牌
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
});

// 响应拦截器：处理错误
request.interceptors.response.use(
  response => {
    const res = response.data;
    // 业务错误处理
    if (res.code === 0) {
      alert(res.msg || '操作失败');
      return Promise.reject(res.msg);
    }
    return res;
  },
  error => {
    // 未登录处理
    if (error.response?.status === 401) {
      alert('请先登录');
      router.push('/login');
    } else {
      alert('请求失败：' + (error.response?.data?.msg || error.message));
    }
    return Promise.reject(error);
  }
);

export default request;