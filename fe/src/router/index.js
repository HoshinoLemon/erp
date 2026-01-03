import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Dashboard from '../views/Dashboard.vue';
import StockAdjust from '../views/StockAdjust.vue';
import StockRecord from '../views/StockRecord.vue';
import SalesOrder from '../views/SalesOrder.vue';
import MainLayout from '../layouts/MainLayout.vue'; // 导入布局组件
import StockManage from '../views/StockManage.vue';
import ProductManage from '../views/ProductManage.vue';
import PurchaseManage from '../views/PurchaseManage.vue';
import StatisticAnalysis from '../views/StatisticAnalysis.vue';
import { getCurrentUser } from '../api/admin'; // 导入用户信息接口

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false, layout: 'fullscreen' }
  },
  {
    path: '/',
    component: MainLayout, // 使用主布局（包含侧边栏）
    meta: { requiresAuth: true },
    children: [
      {
        path: '/',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: '/stock/adjust',
        name: 'StockAdjust',
        component: StockAdjust
      },
      {
        path: '/stock/record',
        name: 'StockRecord',
        component: StockRecord
      },
      { path: '/stock', name: 'StockManage', component: StockManage },
      { path: '/purchase', name: 'Purchase', component: PurchaseManage },
      { path: '/product', name: 'ProductManage', component: ProductManage },
      { path: '/salesOrder', name: 'SalesOrder', component: SalesOrder },
      { path: '/statistic', name: 'StatisticAnalysis', component: StatisticAnalysis }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token');
    if (!token) {
      next('/login');
    } else {
      next();
    }
  } else {
    // 已登录用户访问登录页时跳转到首页
    if (localStorage.getItem('token') && to.path === '/login') {
      next('/');
    } else {
      next();
    }
  }
});

export default router;