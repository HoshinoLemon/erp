<template>
  <div class="container">
    <h2>库存记录管理</h2>
    <div class="filter">
      <input v-model="keyword" placeholder="搜索商品名称" />
      <button @click="getStockRecords">查询</button>
    </div>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>商品名称</th>
          <th>变动数量</th> <!-- 修复：将"库存数量"改为"变动数量" -->
          <th>操作类型</th> <!-- 新增：显示操作类型 -->
          <th>操作时间</th>
          <th>操作人</th>
          <th>备注</th> <!-- 新增：显示备注 -->
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in stockRecords" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.productName }}</td>
          <td :class="item.quantity > 0 ? 'text-green' : 'text-red'">
            {{ item.quantity > 0 ? '+' + item.quantity : item.quantity }}
          </td>
          <td>
            <span v-if="item.type === 1">采购入库</span>
            <span v-if="item.type === 2">销售出库</span>
            <span v-if="item.type === 3">盘点调整</span>
          </td>
          <td>{{ formatTime(item.operateTime) }}</td>
          <td>{{ item.operatorName }}</td>
          <td>{{ item.remark || '-' }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button @click="changePage(1)" :disabled="currentPage === 1">首页</button>
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
      <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">尾页</button>
    </div>
  </div>
</template>

<script>
import { getStockRecordPage } from '../api/stock';

export default {
  data() {
    return {
      stockRecords: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      keyword: ''
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    }
  },
  mounted() {
    this.getStockRecords();
  },
  methods: {
    async getStockRecords() {
      try {
        const res = await getStockRecordPage({
          page: this.currentPage,
          pageSize: this.pageSize,
          keyword: this.keyword
        });
        this.stockRecords = res.data.records;
        this.total = res.data.total;
      } catch (err) {
        console.error('获取库存记录失败', err);
      }
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
      this.getStockRecords();
    },
    formatTime(time) {
      return new Date(time).toLocaleString();
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}
.filter {
  margin: 20px 0;
}
input {
  padding: 8px;
  margin-right: 10px;
  width: 200px;
}
button {
  padding: 8px 16px;
  cursor: pointer;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}
th {
  background-color: #f5f5f5;
}
.pagination {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
}
.text-green {
  color: green;
}
.text-red {
  color: red;
}
</style>