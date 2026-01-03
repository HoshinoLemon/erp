<template>
  <div class="container">
    <h2>采购单管理</h2>
    <div class="filter">
      <input v-model="orderNumber" placeholder="采购单编号" />
      <select v-model="supplierId">
        <option value="">所有供应商</option>
        <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
          {{ supplier.name }}
        </option>
      </select>
      <select v-model="status">
        <option value="">所有状态</option>
        <option value="1">待审核</option>
        <option value="2">已审核</option>
        <option value="3">已完成</option>
        <option value="4">已取消</option>
      </select>
      <button @click="getPurchaseOrders">查询</button>
      <button @click="showAddForm" class="add-btn">新增采购单</button>
    </div>
    
    <!-- 采购单列表表格 -->
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>采购单编号</th>
          <th>供应商</th>
          <th>总金额</th>
          <th>状态</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in purchaseOrders" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.orderNumber }}</td>
          <td>{{ item.supplierName }}</td>
          <td>¥{{ item.totalAmount.toFixed(2) }}</td>
          <td>
            <span :class="statusClass(item.status)">
              {{ statusText(item.status) }}
            </span>
          </td>
          <td>{{ formatTime(item.createTime) }}</td>
          <td>
            <button @click="showDetail(item.id)" class="view-btn">查看</button>
            <button 
              @click="handleAudit(item.id)" 
              class="audit-btn"
              v-if="item.status === 1"
            >
              审核
            </button>
            <button 
              @click="handleComplete(item.id)" 
              class="complete-btn"
              v-if="item.status === 2"
            >
              完成
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
    
    <!-- 新增/编辑采购单表单弹窗 -->
    <div v-if="showForm" class="form-modal">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑采购单' : '新增采购单' }}</h3>
        <div class="form-item">
          <label>供应商</label>
          <select v-model="formData.supplierId">
            <option value="">选择供应商</option>
            <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
              {{ supplier.name }}
            </option>
          </select>
        </div>
        
        <h4>采购商品明细</h4>
        <div class="order-items">
          <div class="item-header">
            <div class="col">商品</div>
            <div class="col">单价</div>
            <div class="col">数量</div>
            <div class="col">金额</div>
            <div class="col">操作</div>
          </div>
          
          <div v-for="(item, index) in formData.items" :key="index" class="item-row">
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
              <input 
                v-model="item.unitPrice" 
                type="number" 
                step="0.01" 
                @change="calculateItemTotal(item)"
              />
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
          采购单总金额: ¥{{ calculateTotalAmount().toFixed(2) }}
        </div>
        
        <div class="form-item">
          <label>备注</label>
          <textarea v-model="formData.remark" rows="3" placeholder="请输入采购备注"></textarea>
        </div>
        
        <div class="form-actions">
          <button @click="cancelForm">取消</button>
          <button @click="submitForm" class="submit-btn">{{ isEdit ? '更新' : '保存' }}</button>
        </div>
      </div>
    </div>
    
    <!-- 采购单详情弹窗 -->
    <div v-if="showDetailModal" class="form-modal">
      <div class="modal-content detail-modal">
        <h3>采购单详情 #{{ currentOrder.orderNumber }}</h3>
        <div class="detail-info">
          <p><strong>供应商:</strong> {{ currentOrder.supplierName }}</p>
          <p><strong>订单状态:</strong> 
            <span :class="statusClass(currentOrder.status)">
              {{ statusText(currentOrder.status) }}
            </span>
          </p>
          <p><strong>创建时间:</strong> {{ formatTime(currentOrder.createTime) }}</p>
          <p v-if="currentOrder.auditTime"><strong>审核时间:</strong> {{ formatTime(currentOrder.auditTime) }}</p>
          <p v-if="currentOrder.completeTime"><strong>完成时间:</strong> {{ formatTime(currentOrder.completeTime) }}</p>
          <p><strong>总金额:</strong> ¥{{ currentOrder.totalAmount.toFixed(2) }}</p>
          <p><strong>备注:</strong> {{ currentOrder.remark || '-' }}</p>
        </div>
        
        <h4>采购商品明细</h4>
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
import { getPurchaseOrderPage, auditPurchaseOrder, completePurchaseOrder, getSupplierList } from '../api/purchase';
import { getProductPage } from '../api/product';
import request from '../api/request';
import { formatTime } from '../utils/date';

export default {
  data() {
    return {
      purchaseOrders: [],
      suppliers: [],
      products: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      orderNumber: '',
      supplierId: '',
      status: '',
      
      showForm: false,
      isEdit: false,
      formData: {
        id: null,
        supplierId: '',
        remark: '',
        items: [],
        totalAmount: 0,
      },
      
      showDetailModal: false,
      currentOrder: {}
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    }
  },
  mounted() {
    this.getPurchaseOrders();
    this.getSuppliers();
    this.getProducts();
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
    // 获取采购单列表
    async getPurchaseOrders() {
      try {
        const res = await getPurchaseOrderPage({
          page: this.currentPage,
          pageSize: this.pageSize,
          orderNumber: this.orderNumber,
          supplierId: this.supplierId,
          status: this.status
        });
        this.purchaseOrders = res.data.records;
        this.total = res.data.total;
      } catch (err) {
        console.error('获取采购单列表失败', err);
      }
    },
    
    // 获取供应商列表
    async getSuppliers() {
      try {
        const res = await getSupplierList();
        this.suppliers = res.data;
      } catch (err) {
        console.error('获取供应商列表失败', err);
      }
    },
    
    // 获取商品列表
    async getProducts() {
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
    
    // 分页切换
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
      this.getPurchaseOrders();
    },
    
    // 显示新增表单
    showAddForm() {
      this.isEdit = false;
      this.formData = {
        id: null,
        supplierId: '',
        remark: '',
        items: [this.createEmptyItem()],
        totalAmount: 0
      };
      this.showForm = true;
    },
    
    // 创建空的商品项
    createEmptyItem() {
      return {
        productId: null,
        productName: '',
        unitPrice: 0,
        quantity: 1,
        totalPrice: 0
      };
    },
    
    // 添加商品项
    addItem() {
      this.formData.items.push(this.createEmptyItem());
    },
    
    // 移除商品项
    removeItem(index) {
      if (this.formData.items.length <= 1) {
        alert('至少保留一个商品');
        return;
      }
      this.formData.items.splice(index, 1);
    },
    
    // 商品选择变化处理
    handleProductChange(item, index) {
      const product = this.products.find(p => p.id === item.productId);
      if (product) {
        item.productName = product.name;
        // 默认使用商品成本价作为采购单价
        item.unitPrice = product.cost;
        item.totalPrice = product.cost * item.quantity;
      } else {
        item.productName = '';
        item.unitPrice = 0;
        item.totalPrice = 0;
      }
    },
    
    // 计算单项总价
    calculateItemTotal(item) {
      item.totalPrice = item.unitPrice * item.quantity;
    },
    
    // 计算订单总价
    calculateTotalAmount() {
      return this.formData.items.reduce((sum, item) => {
        return sum + (item.totalPrice || 0);
      }, 0);
    },
    
    // 取消表单
    cancelForm() {
      this.showForm = false;
    },
    
    // 提交表单
    async submitForm() {
      if (!this.formData.supplierId) {
        alert('请选择供应商');
        return;
      }
      
      const hasValidItem = this.formData.items.some(item => {
        return item.productId && item.quantity > 0 && item.unitPrice > 0;
      });
      
      if (!hasValidItem) {
        alert('请添加有效的商品明细');
        return;
      }
      
      // 修复总金额计算逻辑
      this.formData.totalAmount = this.calculateTotalAmount();
      
      try {
        if (this.isEdit) {
          await request.put('/admin/purchase/order', this.formData);
        } else {
          await request.post('/admin/purchase/order', this.formData);
        }
        this.showForm = false;
        this.getPurchaseOrders(); // 重新加载列表
        alert(this.isEdit ? '采购单更新成功' : '采购单创建成功');
      } catch (err) {
        console.error('提交采购单失败', err);
        alert('操作失败: ' + (err.response?.data?.msg || '系统错误'));
      }
    },
    
    // 查看详情
    async showDetail(id) {
      try {
        const res = await request.get(`/admin/purchase/order/${id}`);
        this.currentOrder = res.data;
        this.showDetailModal = true;
      } catch (err) {
        console.error('获取采购单详情失败', err);
      }
    },
    
    // 关闭详情
    closeDetail() {
      this.showDetailModal = false;
    },
    
    // 审核采购单
    // 审核订单
    async handleAudit(id) {
      if (confirm('确定要审核通过该采购单吗？')) {
        try {
          await auditPurchaseOrder(id);
          this.$message?.success('审核成功');
          this.getPurchaseOrders(); // 调用已实现的列表刷新方法
        } catch (err) {
          // 彻底兼容各种错误对象结构，避免 undefined 错误
          const errorMsg = 
            err?.response?.data?.msg || 
            err?.error?.message || 
            err?.message || 
            '审核失败';
          this.$message.error(errorMsg);
        }
      }
    },
    
    // 完成采购单
    async handleComplete(id) {
      if (confirm('确定要标记该采购单为已完成吗？这将增加相应商品的库存。')) {
        try {
          await completePurchaseOrder(id);
          this.$message?.success('采购单已完成');
          this.getPurchaseOrders();
        } catch (err) {
          // 修正错误信息获取方式，增加更全面的容错
          const errorMsg = err?.response?.data?.msg 
            || err.message 
            || '完成采购单失败';
          this.$message.error(errorMsg);
        }
      }
    },
    
    // 状态文本转换
    statusText(status) {
      const statusMap = {
        1: '待审核',
        2: '已审核',
        3: '已完成',
        4: '已取消'
      };
      return statusMap[status] || '未知';
    },
    
    // 状态样式
    statusClass(status) {
      const classMap = {
        1: 'status-pending',
        2: 'status-audited',
        3: 'status-completed',
        4: 'status-canceled'
      };
      return classMap[status] || '';
    },
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}

.filter {
  margin: 20px 0;
  display: flex;
  gap: 10px;
  align-items: center;
}

.add-btn {
  margin-left: auto;
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
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

th {
  background-color: #f5f5f5;
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

.audit-btn {
  padding: 4px 8px;
  background-color: #f39c12;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.complete-btn {
  padding: 4px 8px;
  background-color: #27ae60;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
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
.form-item select,
.form-item textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
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

.status-pending {
  color: #f39c12;
}

.status-audited {
  color: #3498db;
}

.status-completed {
  color: #27ae60;
}

.status-canceled {
  color: #e74c3c;
}
</style>