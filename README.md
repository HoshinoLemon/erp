# ERP（库存 / 采购 / 销售）示例项目

本项目是一个基础的 ERP 业务示例，包含后端服务与前端管理界面，覆盖库存、采购、销售等常见业务流转与统计能力。

- 后端：Spring Boot + MyBatis + MySQL（Maven 多模块）
- 前端：Vue 3 + Vue Router + Axios + ECharts（Vue CLI）

---

## 目录结构

- `sky-common`：通用工具类、常量、JWT、加密等公共能力
- `sky-pojo`：Entity / DTO / VO 等数据模型
- `sky-server`：后端服务（Spring Boot 启动模块）
- `fe`：前端工程（Vue CLI）

---

## 环境要求

### 后端
- JDK：17（推荐）
- Maven：3.6+
- MySQL：8.x

### 前端
- Node.js：16/18 LTS
- npm：建议 8+

---

## 运行步骤

### 1.克隆并进入项目目录
git clone https://github.com/HoshinoLemon/erp.git
cd erp

### 2.初始化数据库（MySQL），导入 SQL（cmd命令）
mysql -h 127.0.0.1 -P 3306 -u root -p -e "DROP DATABASE IF EXISTS inventory_db;"
### 校验
mysql -h 127.0.0.1 -P 3306 -u root -p -D inventory_db -e "SHOW TABLES;"

### 3.配置后端
后端使用 dev 环境配置（通常为 application-dev.yml），你需要确认并修改数据库连接信息（host/port/username/password/dbname）为你本机实际的 MySQL 配置。
文件位置（按仓库结构）：
sky-server/src/main/resources/application-dev.yml
sky-server/src/main/resources/application.yml（端口、通用配置等）

默认后端端口通常为：8080

### 4.启动后端
mvn -pl sky-server -am spring-boot:run
或在IDEA中找到src/main/java/com/sky/SkyApplication.java并运行

### 5.启动前端
cd fe
npm install
npm run serve

### 6.访问页面
http://localhost:8081

默认测试账号
用户名：admin
密码：123456

###