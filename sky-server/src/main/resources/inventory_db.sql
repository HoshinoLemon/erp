CREATE DATABASE IF NOT EXISTS inventory_db CHARACTER SET utf8mb4;
USE inventory_db;

-- 商品表
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         category_id BIGINT,
                         code VARCHAR(50) UNIQUE NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         cost DECIMAL(10,2) NOT NULL,
                         stock INT NOT NULL DEFAULT 0,
                         status INT NOT NULL DEFAULT 1,
                         image VARCHAR(255),
                         description TEXT,
                         create_time DATETIME NOT NULL,
                         update_time DATETIME NOT NULL,
                         create_user BIGINT,
                         update_user BIGINT
);

-- 库存记录表
CREATE TABLE stock_record (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      product_id BIGINT NOT NULL,
      product_name VARCHAR(100) NOT NULL,
      quantity INT NOT NULL,
      type INT NOT NULL,
      remark TEXT,
      operate_time DATETIME NOT NULL,
      operator_id BIGINT NOT NULL
);

-- 供应商表
CREATE TABLE supplier (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          contact VARCHAR(50),
                          phone VARCHAR(20),
                          address VARCHAR(255),
                          status INT NOT NULL DEFAULT 1,
                          create_time DATETIME NOT NULL,
                          update_time DATETIME NOT NULL
);

-- 采购单表
CREATE TABLE purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    supplier_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status INT NOT NULL,
    create_time DATETIME NOT NULL,
    audit_time DATETIME,
    auditor_id BIGINT
);
-- 销售订单表
CREATE TABLE sales_order (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     order_number VARCHAR(50) UNIQUE NOT NULL,
     customer_name VARCHAR(100) NOT NULL,
     total_amount DECIMAL(10,2) NOT NULL,
     status INT NOT NULL,
     create_time DATETIME NOT NULL,
     completion_time DATETIME,
     operator_id BIGINT NOT NULL
);

-- 销售订单明细表
CREATE TABLE sales_order_item (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      order_id BIGINT NOT NULL,
      product_id BIGINT NOT NULL,
      product_name VARCHAR(100) NOT NULL,
      quantity INT NOT NULL,
      unit_price DECIMAL(10,2) NOT NULL,
      total_price DECIMAL(10,2) NOT NULL,
      FOREIGN KEY (order_id) REFERENCES sales_order(id)
);

-- 商品分类表（新增）
CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL COMMENT '分类名称',
                          sort INT DEFAULT 0 COMMENT '排序序号',
                          parent_id BIGINT COMMENT '父分类ID，用于多级分类',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          UNIQUE KEY uk_name (name) COMMENT '分类名称唯一'
);

-- 为product表的category_id添加外键约束（可选，根据实际业务需求）
ALTER TABLE product
    ADD CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL;