<template>
  <div class="main-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>ERP管理系统</h2>
      </div>
      <!-- erp/fe/src/layouts/MainLayout.vue 侧边栏菜单部分 -->
      <nav class="sidebar-menu">
        <ul>
          <li :class="{ active: $route.path === '/' }">
            <router-link to="/">
              <span class="icon">📊</span>
              <span class="text">首页</span>
            </router-link>
          </li>
          <li :class="{ active: $route.path === '/stock/adjust' }">
            <router-link to="/stock/adjust">
              <span class="icon">📦</span>
              <span class="text">库存调整</span>
            </router-link>
          </li>
          <li :class="{ active: $route.path === '/stock/record' }">
            <router-link to="/stock/record">
              <span class="icon">🔍</span>
              <span class="text">库存记录查询</span>
            </router-link>
          </li>
          <li :class="{ active: $route.path === '/purchase' }">
            <router-link to="/purchase">
              <span class="icon">📝</span>
              <span class="text">采购管理</span>
            </router-link>
          </li>
          <!-- 新增：商品管理菜单 -->
          <li :class="{ active: $route.path === '/product' }">
            <router-link to="/product">
              <span class="icon">📝</span>
              <span class="text">商品管理</span>
            </router-link>
          </li>
          <li :class="{ active: $route.path === '/salesOrder' }">
            <router-link to="/salesOrder">
              <span class="icon">📝</span>
              <span class="text">销售管理</span>
            </router-link>
          </li>
          <!-- 新增：统计分析菜单 -->
          <li :class="{ active: $route.path === '/statistic' }">
            <router-link to="/statistic">
              <span class="icon">📈</span>
              <span class="text">统计分析</span>
            </router-link>
          </li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <button @click="handleLogout" class="logout-btn">
          <span class="icon">🚪</span>
          <span class="text">退出登录</span>
        </button>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  methods: {
    handleLogout() {
      localStorage.removeItem('token');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.main-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 220px;
  background-color: #2c3e50;
  color: white;
  display: flex;
  flex-direction: column;
  height: 100vh;
  position: fixed;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
}

.sidebar-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-menu li {
  margin: 0;
}

.sidebar-menu a {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: all 0.3s;
}

.sidebar-menu a:hover,
.sidebar-menu .active a {
  background-color: #34495e;
  color: white;
}

.icon {
  margin-right: 10px;
  width: 20px;
  text-align: center;
}

.sidebar-footer {
  margin-top: auto;
  padding: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.logout-btn {
  width: 100%;
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  color: white;
  background-color: #e74c3c;
  border-radius: 4px;
}

.main-content {
  flex: 1;
  margin-left: 220px;
  padding: 20px;
}
</style>