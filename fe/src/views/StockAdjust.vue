<template>
  <div class="stock-adjust">
    <h2>库存调整</h2>
    <div class="adjust-form">
      <div class="form-item">
        <label>商品选择</label>
        <select v-model="selectedProductId" @change="handleProductChange">
          <option value="">-- 选择商品 --</option>
          <option v-for="prod in products" :key="prod.id" :value="prod.id">
  {{ prod.name }} (当前库存: {{ prod.stock }})
</option>
        </select>
      </div>
      
      <div class="form-item">
        <label>调整数量</label>
        <input 
          type="number" 
          v-model="adjustQuantity" 
          placeholder="正数增加，负数减少"
        >
      </div>
      
      <div class="form-item">
        <label>备注</label>
        <textarea v-model="remark" rows="3" placeholder="请输入调整原因"></textarea>
      </div>
      
      <button @click="submitAdjust" class="submit-btn">提交调整</button>
    </div>
    
    <div v-if="adjustResult" class="result-alert">
      {{ adjustResult }}
    </div>
  </div>
</template>

<script>
import request from '../api/request';

export default {
  data() {
    return {
      products: [],
      selectedProductId: '',
      currentStock: 0,
      adjustQuantity: 0,
      remark: '',
      adjustResult: ''
    };
  },
  mounted() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts() {
      try {
        const res = await request.get('/admin/product/list');
        this.products = res.data;
      } catch (err) {
        console.error('获取商品列表失败', err);
      }
    },
    handleProductChange() {
      const prod = this.products.find(p => p.id === this.selectedProductId);
      this.currentStock = prod ? prod.stock : 0;
    },
    async submitAdjust() {
      if (!this.selectedProductId || this.adjustQuantity === 0) {
        this.adjustResult = '请选择商品并输入调整数量';
        return;
      }
      
      try {
        await request.post('/admin/product/stock/adjust', {
          productId: this.selectedProductId,
          quantity: this.adjustQuantity,
          remark: this.remark
        });
        this.adjustResult = '库存调整成功！';
        // 重新获取商品列表刷新库存
        this.fetchProducts();
        // 重置表单
        this.selectedProductId = '';
        this.adjustQuantity = 0;
        this.remark = '';
      } catch (err) {
        this.adjustResult = '调整失败：' + (err.response?.data?.msg || '系统错误');
      }
    }
  }
};
</script>

<style scoped>
.stock-adjust {
  padding: 20px;
}

.adjust-form {
  max-width: 600px;
  margin: 20px 0;
}

.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-item select,
.form-item input,
.form-item textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.submit-btn {
  padding: 10px 20px;
  background-color: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #27ae60;
}

.result-alert {
  margin-top: 20px;
  padding: 15px;
  border-radius: 4px;
  background-color: #ecf0f1;
}
</style>