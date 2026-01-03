<template>
  <div class="container">
    <h2>数据统计分析</h2>
    <div class="date-filter">
      <input type="date" v-model="startDate" />
      <span>至</span>
      <input type="date" v-model="endDate" />
      <button @click="fetchStatistics">查询</button>
    </div>

    <!-- 销售统计图表 -->
    <div class="chart-box">
      <h3>销售统计</h3>
      <div ref="salesChart" class="chart"></div>
    </div>

    <!-- 采购统计图表 -->
    <div class="chart-box">
      <h3>采购统计</h3>
      <div ref="purchaseChart" class="chart"></div>
    </div>

    <!-- 库存统计图表 -->
    <div class="chart-box">
      <h3>库存统计</h3>
      <div ref="stockChart" class="chart"></div>
    </div>

    <!-- 商品销售排行 -->
    <div class="chart-box">
      <h3>商品销售排行（Top {{ topN }}）</h3>
      <div ref="rankChart" class="chart"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import request from '../api/request';

export default {
  data() {
    return {
      startDate: '',
      endDate: '',
      topN: 10,
      salesChart: null,
      purchaseChart: null,
      stockChart: null,
      rankChart: null
    };
  },
  mounted() {
    // 初始化图表
    this.salesChart = echarts.init(this.$refs.salesChart);
    this.purchaseChart = echarts.init(this.$refs.purchaseChart);
    this.stockChart = echarts.init(this.$refs.stockChart);
    this.rankChart = echarts.init(this.$refs.rankChart);

    // 设置默认日期范围（近30天）
    const end = new Date();
    const start = new Date();
    start.setDate(start.getDate() - 30);
    this.startDate = this.formatDate(start);
    this.endDate = this.formatDate(end);

    this.fetchStatistics();
  },
  methods: {
    formatDate(date) {
      return date.toISOString().split('T')[0];
    },
    async fetchStatistics() {
      try {
        // 转换为 "yyyy-MM-dd HH:mm:ss" 格式
        const format = (date) => {
          const d = new Date(date);
          return d.toISOString().slice(0, 10) + ' ' + d.toTimeString().slice(0, 8);
        };
        const start = format(this.startDate);
        const end = format(this.endDate);

        // 1. 销售统计
        const salesRes = await request.get('/admin/statistic/sales', {
          params: { start, end }
        });
        this.updateSalesChart(salesRes.data);

        // 2. 采购统计
        const purchaseRes = await request.get('/admin/statistic/purchase', {
          params: { start, end }
        });
        this.updatePurchaseChart(purchaseRes.data);

        // 3. 库存统计
        const stockRes = await request.get('/admin/statistic/stock');
        this.updateStockChart(stockRes.data);

        // // 4. 商品销售排行
        // const rankRes = await request.get('/admin/statistic/product/rank', {
        //   params: { start, end, top: this.topN }
        // });
        // this.updateRankChart(rankRes.data);

      } catch (err) {
        console.error('获取统计数据失败', err);
      }
    },
    updateSalesChart(data) {
      // 从 dailyList 提取日期和金额（处理空数组情况）
      const dates = data.dailyList.map(item => item.date) || [];
      const amounts = data.dailyList.map(item => item.amount) || [];
      
      this.salesChart.setOption({
        title: { text: '销售额趋势' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', name: '销售额(元)' },
        series: [{
          data: amounts,
          type: 'line',
          smooth: true
        }]
      });
    },
    updatePurchaseChart(data) {
      const dates = data.dailyList.map(item => item.date) || [];
      const amounts = data.dailyList.map(item => item.amount) || [];
      
      this.purchaseChart.setOption({
        title: { text: '采购额趋势' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', name: '采购额(元)' },
        series: [{
          data: amounts,
          type: 'bar'
        }]
      });
    },
    updateStockChart(data) {
      this.stockChart.setOption({
        title: { text: '库存分类占比' },
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          data: data.categoryStockList.map(item => ({  // 修正为 categoryStockList
            name: item.categoryName,
            value: item.stockQuantity  // 匹配后端的 stockQuantity 字段
          }))
        }]
      });
    },
    // updateRankChart(data) {
    //   this.rankChart.setOption({
    //     title: { text: '销量排行' },
    //     xAxis: { type: 'value', name: '销量' },
    //     yAxis: { 
    //       type: 'category', 
    //       data: data.map(item => item.productName),
    //       inverse: true
    //     },
    //     series: [{
    //       type: 'bar',
    //       data: data.map(item => item.salesVolume)  // 修正为 salesVolume（后端返回的字段）
    //     }]
    //   });
    // }
  },
  beforeUnmount() {
    // 销毁图表实例
    this.salesChart.dispose();
    this.purchaseChart.dispose();
    this.stockChart.dispose();
    // this.rankChart.dispose();
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}
.date-filter {
  margin: 20px 0;
  display: flex;
  gap: 10px;
  align-items: center;
}
.chart-box {
  margin: 30px 0;
}
.chart {
  width: 100%;
  height: 400px;
  border: 1px solid #eee;
  border-radius: 4px;
}
</style>