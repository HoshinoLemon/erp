<template>
  <div class="container">
    <h2>商品管理</h2>
    <div class="filter">
      <input v-model="name" placeholder="商品名称" />
      <input v-model="code" placeholder="商品编码" />
      <button @click="getProducts">查询</button>
      <button @click="showAddForm" class="add-btn">新增商品</button>
    </div>
    
    <!-- 商品列表表格 -->
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>名称</th>
          <th>编码</th>
          <th>价格</th>
          <th>库存</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in products" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.code }}</td>
          <td>¥{{ item.price.toFixed(2) }}</td>
          <td>{{ item.stock }}</td>
          <td>{{ item.status === 1 ? '启用' : '禁用' }}</td>
          <td>
            <button @click="showEditForm(item)" class="edit-btn">编辑</button>
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
    
    <!-- 新增/编辑商品表单弹窗 -->
    <div v-if="showForm" class="form-modal">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑商品' : '新增商品' }}</h3>
        <div class="form-item">
          <label>商品名称</label>
          <input v-model="formData.name" placeholder="请输入商品名称" />
        </div>
        <div class="form-item">
          <label>商品编码</label>
          <input v-model="formData.code" placeholder="请输入商品编码" :disabled="isEdit" />
        </div>
        <div class="form-item">
          <label>商品分类ID</label>
          <input v-model="formData.categoryId" type="number" placeholder="请输入分类ID" />
        </div>
        <div class="form-item">
          <label>销售价格</label>
          <input v-model="formData.price" type="number" step="0.01" placeholder="请输入销售价格" />
        </div>
        <div class="form-item">
          <label>成本价格</label>
          <input v-model="formData.cost" type="number" step="0.01" placeholder="请输入成本价格" />
        </div>
        <div class="form-item">
          <label>库存数量</label>
          <input v-model="formData.stock" type="number" placeholder="请输入库存数量" />
        </div>
        <div class="form-item">
          <label>商品状态</label>
          <select v-model="formData.status">
            <option value="1">启用</option>
            <option value="0">禁用</option>
          </select>
        </div>
        <div class="form-item">
          <label>商品描述</label>
          <textarea v-model="formData.description" rows="3" placeholder="请输入商品描述"></textarea>
        </div>
        <div class="form-actions">
          <button @click="cancelForm">取消</button>
          <button @click="submitForm" class="submit-btn">{{ isEdit ? '更新' : '保存' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getProductPage, getProductById } from '../api/product';
import request from '../api/request';

export default {
  data() {
    return {
      products: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      name: '',
      code: '',
      showForm: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        code: '',
        categoryId: null,
        price: null,
        cost: null,
        stock: 0,
        status: 1,
        description: ''
      }
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    }
  },
  mounted() {
    this.getProducts();
  },
  methods: {
    async getProducts() {
      try {
        const res = await getProductPage({
          page: this.currentPage,
          pageSize: this.pageSize,
          name: this.name,
          code: this.code
        });
        this.products = res.data.records;
        this.total = res.data.total;
      } catch (err) {
        console.error('获取商品列表失败', err);
      }
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
      this.getProducts();
    },
    showAddForm() {
      this.isEdit = false;
      this.formData = {
        id: null,
        name: '',
        code: '',
        categoryId: null,
        price: null,
        cost: null,
        stock: 0,
        status: 1,
        description: ''
      };
      this.showForm = true;
    },
    async showEditForm(item) {
      this.isEdit = true;
      try {
        const res = await getProductById(item.id);
        this.formData = res.data;
        this.showForm = true;
      } catch (err) {
        console.error('获取商品详情失败', err);
      }
    },
    cancelForm() {
      this.showForm = false;
    },
    async submitForm() {
      if (!this.formData.name || !this.formData.code && !this.isEdit) {
        alert('商品名称和编码不能为空');
        return;
      }
      
      try {
        if (this.isEdit) {
          await request.put('/admin/product', this.formData);
        } else {
          await request.post('/admin/product', this.formData);
        }
        this.showForm = false;
        this.getProducts(); // 重新加载列表
        alert(this.isEdit ? '商品更新成功' : '商品新增成功');
      } catch (err) {
        console.error('提交商品信息失败', err);
        alert('操作失败: ' + (err.response?.data?.msg || '系统错误'));
      }
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
input {
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
.edit-btn {
  padding: 4px 8px;
  background-color: #3498db;
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
}
.modal-content {
  background-color: white;
  padding: 20px;
  width: 500px;
  border-radius: 8px;
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
</style>