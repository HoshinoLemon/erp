<template>
  <div class="container">
    <h2>销售单管理</h2>
    <button @click="showAddOrderForm" class="add-btn">新增销售单</button>
    
    <!-- 销售单列表 -->
    <div class="filter">
      <input v-model="customerName" placeholder="客户名称" />
      <select v-model="status">
        <option value="">全部状态</option>
        <option value="1">待发货</option>
        <option value="2">已完成</option>
        <option value="3">已取消</option>
      </select>
      <button @click="getOrders">查询</button>
    </div>
    
    <table>
      <thead>
        <tr>
          <th>订单编号</th>
          <th>客户名称</th>
          <th>总金额</th>
          <th>状态</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in orders" :key="item.id">
          <td>{{ item.orderNumber }}</td>
          <td>{{ item.customerName }}</td>
          <td>¥{{ item.totalAmount.toFixed(2) }}</td>
          <td>
            <span v-if="item.status === 1" class="status-pending">待发货</span>
            <span v-if="item.status === 2" class="status-completed">已完成</span>
            <span v-if="item.status === 3" class="status-canceled">已取消</span>
          </td>
          <td>{{ formatTime(item.createTime) }}</td>
          <td>
            <button @click="viewOrderDetail(item.id)" class="view-btn">查看</button>
            <button 
              @click="completeOrder(item.id)" 
              class="complete-btn"
              v-if="item.status === 1"
            >
              完成订单
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <!-- 分页控件 -->
    <div class="pagination">
      <button @click="changePage(1)" :disabled="currentPage === 1">首页</button>
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
      <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">尾页</button>
    </div>
    
    <!-- 新增销售单表单 -->
    <div v-if="showOrderForm" class="form-modal">
      <div class="modal-content">
        <h3>新增销售单</h3>
        <div class="form-item">
          <label>客户名称</label>
          <input v-model="orderForm.customerName" placeholder="请输入客户名称" />
        </div>
        
        <h4>商品明细</h4>
        <div class="order-items">
          <div class="item-header">
            <div class="col">商品</div>
            <div class="col">单价</div>
            <div class="col">金额</div>
            <div class="col">数量</div>
            <div class="col">操作</div>
          </div>
          
          <div v-for="(item, index) in orderForm.items" :key="index" class="item-row">
            <div class="col">
              <select 
                v-model="item.productId" 
                @change="handleProductChange(item, index)"
              >
                <option value="">-- 选择商品 --</option>
                <option v-for="prod in products" :key="prod.id" :value="prod.id">
                  {{ prod.name }}
                </option>
              </select>
            </div>
            <div class="col">
              <input v-model="item.unitPrice" type="number" step="0.01" readonly />
            </div>
            <div class="col">
              <input 
                v-model="item.quantity" 
                type="number" 
                min="1" 
                @change="calculateItemTotal(item)"
              />
            </div>
            <div class="col">¥{{ item.totalPrice ? item.totalPrice.toFixed(2) : '0.00' }}</div>
            <div class="col">
              <button @click="removeItem(index)" class="remove-btn">删除</button>
            </div>
          </div>
          
          <button @click="addItem" class="add-item-btn">+ 添加商品</button>
        </div>
        
        <div class="total-amount">
          订单总金额: ¥{{ calculateTotalAmount().toFixed(2) }}
        </div>
        
        <div class="form-actions">
          <button @click="cancelOrderForm">取消</button>
          <button @click="submitOrder" class="submit-btn">提交订单</button>
        </div>
      </div>
    </div>
    
    <!-- 订单详情弹窗 -->
    <div v-if="showOrderDetail" class="form-modal">
      <div class="modal-content detail-modal">
        <h3>订单详情 #{{ currentOrder.orderNumber }}</h3>
        <div class="detail-info">
          <p><strong>客户名称:</strong> {{ currentOrder.customerName }}</p>
          <p><strong>订单状态:</strong> 
            <span v-if="currentOrder.status === 1" class="status-pending">待发货</span>
            <span v-if="currentOrder.status === 2" class="status-completed">已完成</span>
            <span v-if="currentOrder.status === 3" class="status-canceled">已取消</span>
          </p>
          <p><strong>创建时间:</strong> {{ formatTime(currentOrder.createTime) }}</p>
          <p v-if="currentOrder.completionTime"><strong>完成时间:</strong> {{ formatTime(currentOrder.completionTime) }}</p>
          <p><strong>总金额:</strong> ¥{{ currentOrder.totalAmount.toFixed(2) }}</p>
        </div>
        
        <h4>商品明细</h4>
        <table class="detail-table">
          <thead>
            <tr>
              <th>商品名称</th>
              <th>单价</th>
              <th>数量</th>
              <th>金额</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in currentOrder.items" :key="item.id">
              <td>{{ item.productName }}</td>
              <td>¥{{ item.unitPrice.toFixed(2) }}</td>
              <td>{{ item.quantity }}</td>
              <td>¥{{ item.totalPrice.toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
        
        <div class="form-actions">
          <button @click="closeDetail">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '../api/request';
import { getProductPage } from '../api/product';

export default {
  data() {
    return {
      orders: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      customerName: '',
      status: '',
      products: [],
      
      showOrderForm: false,
      orderForm: {
        customerName: '',
        items: []
      },
      
      showOrderDetail: false,
      currentOrder: {}
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    }
  },
  mounted() {
    this.getOrders();
    this.getProductsList();
  },
  methods: {
    formatTime(timeArr) {
      // 校验数组有效性（至少包含年月日时分）
      if (!timeArr || timeArr.length < 5) return 'Invalid Date';
      
      // 解构数组元素（年、月、日、时、分），补全秒为0
      const [year, month, day, hour, minute] = timeArr;
      const second = timeArr[5] || 0; // 若缺少秒，默认补0
      
      // 月份是从0开始的，需减1（关键！否则月份会多1）
      const date = new Date(year, month - 1, day, hour, minute, second);
      
      // 格式化日期为 "yyyy-MM-dd HH:mm:ss"
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      }).replace(/\//g, '-'); // 将斜杠替换为横杠
    },
    async getOrders() {
      try {
        const res = await request.get('/admin/sales/order/page', {
          params: {
            page: this.currentPage,
            pageSize: this.pageSize,
            customerName: this.customerName,
            status: this.status
          }
        });
        this.orders = res.data.records;
        this.total = res.data.total;
      } catch (err) {
        console.error('获取销售订单失败', err);
      }
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
      this.getOrders();
    },
    async getProductsList() {
      try {
        const res = await getProductPage({
          page: 1,
          pageSize: 100 // 获取足够多的商品供选择
        });
        this.products = res.data.records;
      } catch (err) {
        console.error('获取商品列表失败', err);
      }
    },
    showAddOrderForm() {
      this.orderForm = {
        customerName: '',
        items: [this.createEmptyItem()]
      };
      this.showOrderForm = true;
    },
    createEmptyItem() {
      return {
        productId: null,
        productName: '',
        unitPrice: 0,
        quantity: 1,
        totalPrice: 0
      };
    },
    addItem() {
      this.orderForm.items.push(this.createEmptyItem());
    },
    removeItem(index) {
      if (this.orderForm.items.length <= 1) {
        alert('至少保留一个商品');
        return;
      }
      this.orderForm.items.splice(index, 1);
    },
    handleProductChange(item, index) {
      const product = this.products.find(p => p.id === item.productId);
      if (product) {
        item.productName = product.name;
        item.unitPrice = product.price;
        item.totalPrice = product.price * item.quantity;
      } else {
        item.productName = '';
        item.unitPrice = 0;
        item.totalPrice = 0;
      }
    },
    calculateItemTotal(item) {
      item.totalPrice = item.unitPrice * item.quantity;
    },
    calculateTotalAmount() {
      return this.orderForm.items.reduce((sum, item) => {
        return sum + (item.totalPrice || 0);
      }, 0);
    },
    cancelOrderForm() {
      this.showOrderForm = false;
    },
    async submitOrder() {
      if (!this.orderForm.customerName) {
        alert('请输入客户名称');
        return;
      }
      
      const hasValidItem = this.orderForm.items.some(item => {
        return item.productId && item.quantity > 0;
      });
      
      if (!hasValidItem) {
        alert('请添加有效的商品明细');
        return;
      }
      
      try {
        await request.post('/admin/sales/order', this.orderForm);
        this.showOrderForm = false;
        this.getOrders(); // 重新加载订单列表
        alert('销售订单创建成功');
      } catch (err) {
        console.error('创建销售订单失败', err);
        alert('操作失败: ' + (err.response?.data?.msg || '系统错误'));
      }
    },
    async viewOrderDetail(id) {
      try {
        const res = await request.get(`/admin/sales/order/${id}`);
        this.currentOrder = res.data;
        this.showOrderDetail = true;
      } catch (err) {
        console.error('获取订单详情失败', err);
      }
    },
    closeDetail() {
      this.showOrderDetail = false;
    },
    async completeOrder(id) {
      if (confirm('确定要完成此订单吗？这将扣减相应商品的库存。')) {
        try {
          await request.put(`/admin/sales/order/complete/${id}`);
          this.getOrders();
          alert('订单已完成');
        } catch (err) {
          console.error('完成订单失败', err);
          alert('操作失败: ' + (err.response?.data?.msg || '系统错误'));
        }
      }
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}
.add-btn {
  margin-bottom: 20px;
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
.filter {
  margin: 20px 0;
  display: flex;
  gap: 10px;
  align-items: center;
}
input, select {
  padding: 8px;
  width: 200px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}
th, td {
  border: 1px solid #ddd;
  padding: 12px;
}
.status-pending {
  color: #f39c12;
}
.status-completed {
  color: #27ae60;
}
.status-canceled {
  color: #e74c3c;
}
.view-btn {
  padding: 4px 8px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}
.complete-btn {
  padding: 4px 8px;
  background-color: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.pagination {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
.form-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-y: auto;
}
.modal-content {
  background-color: white;
  padding: 20px;
  width: 800px;
  border-radius: 8px;
  margin: 20px;
}
.detail-modal {
  width: 900px;
}
.form-item {
  margin-bottom: 15px;
}
.form-item label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}
.form-item input,
.form-item select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.order-items {
  margin: 20px 0;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}
.item-header, .item-row {
  display: flex;
}
.col {
  flex: 1;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}
.item-header {
  background-color: #f5f5f5;
  font-weight: bold;
}
.item-row:last-child .col {
  border-bottom: none;
}
.remove-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}
.add-item-btn {
  margin: 10px;
  padding: 8px 16px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.total-amount {
  text-align: right;
  font-size: 18px;
  font-weight: bold;
  margin: 15px 0;
  color: #e74c3c;
}
.form-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.form-actions button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}
.submit-btn {
  background-color: #2ecc71;
  color: white;
  border: none;
}
.detail-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}
.detail-info p {
  margin: 8px 0;
}
.detail-table {
  width: 100%;
  margin: 15px 0;
}
</style>