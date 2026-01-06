CREATE DATABASE IF NOT EXISTS inventory_db CHARACTER SET utf8mb4;
USE inventory_db;

-- 商品分类表
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    sort INT DEFAULT 0 COMMENT '排序序号',
    parent_id BIGINT COMMENT '父分类ID，用于多级分类',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name) COMMENT '分类名称唯一'
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

-- 员工表
CREATE TABLE employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(64),
    phone VARCHAR(32),
    status INT DEFAULT 1,
    role VARCHAR(32) DEFAULT 'ADMIN',
    create_time DATETIME,
    update_time DATETIME,
    create_user BIGINT,
    update_user BIGINT
);

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

-- 采购单明细表
CREATE TABLE purchase_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(128),
    quantity INT,
    unit_price DECIMAL(10,2),
    total_price DECIMAL(10,2),
    INDEX idx_poi_order_id(order_id),
    CONSTRAINT fk_poi_order FOREIGN KEY(order_id) REFERENCES purchase_order(id)
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

-- 库存记录表
CREATE TABLE stock_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    type INT NOT NULL,
    remark TEXT,
    operate_time DATETIME NOT NULL,
    operator_id BIGINT NOT NULL,
    total_after_alter BIGINT NULL
);

-- 为product表的category_id添加外键约束
ALTER TABLE product
    ADD CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL;

-- 初始化测试管理员账号（username=admin, password=123456）
-- 说明：password 使用 BCrypt 哈希（与后端 BCrypt.checkpw 兼容）
INSERT INTO employee (username, password, status, role, create_time, update_time)
VALUES (
    'admin',
    '$2a$10$v28q8A/KJ0uRUf85hRbo9.gztY1tDCzUVMDFI/vOB8sAKU0KMjbAu',
    1,
    'ADMIN',
    NOW(),
    NOW()
)
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    status = VALUES(status),
    role = VALUES(role),
    update_time = VALUES(update_time);
