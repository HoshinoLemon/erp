<template>
  <div class="stock-warning">
    <h2>库存预警</h2>
    <table>
      <thead>
        <tr>
          <th>商品ID</th>
          <th>商品名称</th>
          <th>当前库存</th>
          <th>预警阈值</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in warningProducts" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td class="text-red">{{ item.stock }}</td>
          <td>{{ item.warningThreshold }}</td>
          <td>
            <button @click="goPurchase(item.id)">生成采购单</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import request from '../api/request';
import { useRouter } from 'vue-router';

export default {
  data() {
    return {
      warningProducts: []
    };
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  mounted() {
    this.getWarningProducts();
  },
  methods: {
    async getWarningProducts() {
      try {
        const res = await request.get('/admin/product/warning');
        this.warningProducts = res.data;
      } catch (err) {
        console.error('获取库存预警商品失败', err);
      }
    },
    goPurchase(productId) {
      this.router.push({
        path: '/purchase/create',
        query: { productId }
      });
    }
  }
};
</script>

<style scoped>
.stock-warning {
  padding: 20px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}
th, td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}
th {
  background-color: #f5f5f5;
}
.text-red {
  color: red;
  font-weight: bold;
}
button {
  padding: 6px 12px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>