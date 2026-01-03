<template>
  <div class="stock-record">
    <h2>库存调整记录查询</h2>
    <div class="filter-bar">
      <input 
        v-model="params.productId" 
        type="number" 
        placeholder="商品ID（可选）" 
      />
      <button @click="fetchRecords">查询</button>
    </div>
    
    <table class="record-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>商品名称</th>
          <th>调整数量</th>
          <th>调整后库存</th>
          <th>操作时间</th>
          <th>备注</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in records" :key="record.id">
          <td>{{ record.id }}</td>
          <td>{{ record.productName }}</td>
          <td :style="{ color: record.quantity > 0 ? 'green' : 'red' }">
            {{ record.quantity > 0 ? '+' : '' }}{{ record.quantity }}
          </td>
          <td>{{ record.totalAfterAlter }}</td>
          <td>{{ formatTime(record.operateTime) }}</td>
          <td>{{ record.remark || '-' }}</td>
        </tr>
      </tbody>
    </table>
    
    <div class="pagination">
      <button 
        @click="params.page = Math.max(1, params.page - 1)" 
        :disabled="params.page === 1"
      >
        上一页
      </button>
      <span>第 {{ params.page }} 页</span>
      <button @click="params.page += 1">下一页</button>
    </div>
  </div>
</template>

<script>
import { getStockRecordPage } from '../api/stock';

export default {
  data() {
    return {
      params: {
        page: 1,
        pageSize: 10,
        productId: ''
      },
      records: [],
      total: 0
    };
  },
  methods: {
    async fetchRecords() {
      try {
        const res = await getStockRecordPage(this.params);
        this.records = res.data.records;
        this.total = res.data.total;
      } catch (err) {
        console.error('查询记录失败', err);
      }
    },
    formatTime(timeArr) {
      // 转换后端返回的时间数组 [年,月,日,时,分,秒]
      if (!timeArr || timeArr.length < 6) return '';
      return `${timeArr[0]}-${this.padZero(timeArr[1])}-${this.padZero(timeArr[2])} ${this.padZero(timeArr[3])}:${this.padZero(timeArr[4])}:${this.padZero(timeArr[5])}`;
    },
    padZero(num) {
      return num.toString().padStart(2, '0');
    }
  },
  mounted() {
    this.fetchRecords();
  },
  watch: {
    'params.page': {
      handler() {
        this.fetchRecords();
      }
    }
  }
};
</script>

<style scoped>
.stock-record {
  padding: 20px;
}
.filter-bar {
  margin: 15px 0;
  display: flex;
  gap: 10px;
}
.filter-bar input {
  flex: 1;
  max-width: 300px;
  padding: 8px;
}
.record-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}
.record-table th, .record-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}
.record-table th {
  background-color: #f5f5f5;
}
.pagination {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
}
.pagination button {
  padding: 5px 10px;
  cursor: pointer;
}
</style>