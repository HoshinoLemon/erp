<template>
  <div class="login-container">
    <div class="login-box">
      <h2>ERP管理系统 - 登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="username" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="password" required>
        </div>
        <button type="submit" class="login-btn">登录</button>
      </form>
    </div>
  </div>
</template>

<script>
import request from '../api/request';

export default {
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    async handleLogin() {
      try {
        const res = await request.post('/admin/employee/login', {
          username: this.username,
          password: this.password
        });
        localStorage.setItem('token', res.data.token);
        this.$router.push('/');
      } catch (err) {
        alert('登录失败：' + (err.response?.data?.msg || '用户名或密码错误'));
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-box {
  width: 350px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  padding: 10px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.login-btn:hover {
  background-color: #2980b9;
}
</style>