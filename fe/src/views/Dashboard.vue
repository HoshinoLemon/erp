<template>
  <div class="dashboard">
    <h1>ERP管理系统首页</h1>
    
    <div class="stats-grid">
      <div class="stat-card">
        <h3>总商品数</h3>
        <p class="stat-value">{{ stockStat?.productCount || 0 }}</p>
        <p class="stat-desc">较昨日 +{{ productCountChange }}</p>
      </div>
      <div class="stat-card">
        <h3>今日调整次数</h3>
        <p class="stat-value">{{ todayAdjustCount }}</p>
        <p class="stat-desc">较昨日 {{ todayAdjustChange > 0 ? '+' : '' }}{{ todayAdjustChange }}</p>
      </div>
      <div class="stat-card">
        <h3>库存预警商品</h3>
        <p class="stat-value">{{ stockStat?.warningCount || 0 }}</p>
        <p class="stat-desc">需要及时补货</p>
      </div>
      <div class="stat-card">
        <h3>总库存数量</h3>
        <p class="stat-value">{{ stockStat?.totalStock || 0 }}</p>
        <p class="stat-desc">件</p>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-item">
        <h3>库存分类占比</h3>
        <div ref="categoryChart" class="small-chart"></div>
      </div>
      <div class="chart-item">
        <h3>近期销售趋势</h3>
        <div ref="salesTrendChart" class="small-chart"></div>
      </div>
    </div>

    <div class="recent-activities">
      <h2>最近调整记录</h2>
      <table class="recent-table">
        <thead>
          <tr>
            <th>商品ID</th>
            <th>商品名称</th>
            <th>调整数量</th>
            <th>操作时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in recentRecords" :key="item.id">
            <td>{{ item.productId }}</td>
            <td>{{ item.productName }}</td>
            <td :style="{ color: item.quantity > 0 ? 'green' : 'red' }">
              {{ item.quantity > 0 ? '+' : '' }}{{ item.quantity }}
            </td>
            <td>{{ item.time }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getStockStatistic } from '../api/statistic';
import { getStockRecordPage } from '../api/stock';
import { getSalesStatistic } from '../api/statistic';

export default {
  data() {
    return {
      stockStat: null,
      recentRecords: [],
      productCountChange: 0,
      todayAdjustCount: 0,
      todayAdjustChange: 0,
      categoryChart: null,
      salesTrendChart: null
    };
  },
  mounted() {
    this.initCharts();
    this.fetchDashboardData();
  },
  methods: {
    initCharts() {
      this.categoryChart = echarts.init(this.$refs.categoryChart);
      this.salesTrendChart = echarts.init(this.$refs.salesTrendChart);
    },
    async fetchDashboardData() {
      // 1. 获取库存统计数据
      const stockRes = await getStockStatistic();
      this.stockStat = stockRes.data;
      this.updateCategoryChart();

      // 2. 获取最近调整记录
      const recordRes = await getStockRecordPage({ page: 1, pageSize: 5 });
      this.recentRecords = recordRes.data.records;

      // 3. 获取今日销售趋势（近7天）
      const end = new Date();
      const start = new Date();
      start.setDate(start.getDate() - 7);
      const salesRes = await getSalesStatistic({
        start: start.toISOString(),
        end: end.toISOString()
      });
      this.updateSalesTrendChart(salesRes.data);

      // 模拟计算变化值
      this.productCountChange = Math.floor(Math.random() * 20);
      this.todayAdjustCount = this.recentRecords.length;
      this.todayAdjustChange = Math.floor(Math.random() * 10) - 3;
    },
    updateCategoryChart() {
      if (!this.stockStat?.categoryStockList) {
        console.log('无库存分类数据');
        return;
      }
      
      this.categoryChart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: '70%',
          data: this.stockStat.categoryStockList.map(item => ({
            name: item.categoryName,
            value: item.stockQuantity
          }))
        }]
      });
    },
    updateSalesTrendChart(data) {
      // 从 dailyList 中提取日期和金额（处理空数组情况）
      const dates = data.dailyList.map(item => item.date) || [];
      const amounts = data.dailyList.map(item => item.amount) || [];
      
      this.salesTrendChart.setOption({
        xAxis: { type: 'category', data: dates },  // 使用提取的日期数组
        yAxis: { type: 'value', name: '销售额' },
        series: [{
          data: amounts,  // 使用提取的金额数组
          type: 'line',
          smooth: true,
          lineStyle: { width: 3 }
        }]
      });
    }
  },
  beforeUnmount() {
    this.categoryChart?.dispose();
    this.salesTrendChart?.dispose();
  }
};
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin: 30px 0;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin: 10px 0;
}

.stat-desc {
  color: #666;
  font-size: 14px;
}

.charts-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin: 30px 0;
}

.chart-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.small-chart {
  width: 100%;
  height: 300px;
}

.recent-activities {
  margin-top: 40px;
}

.recent-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

.recent-table th, .recent-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.recent-table th {
  background-color: #f5f5f5;
}
</style>