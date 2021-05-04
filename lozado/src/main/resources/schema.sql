CREATE TABLE IF NOT EXISTS app_user(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    user_name  VARCHAR(255),
    x_api_key VARCHAR(255),
    created_at    TIMESTAMP default now()
);

CREATE TABLE IF NOT EXISTS categories(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS suppliers(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255),
    address VARCHAR(255),
    phone   VARCHAR(255),
    account_no  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS brands(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255),
    country VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS products(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255),
    price   NUMERIC(18,2),
    discount    NUMERIC(18,2),
    promotion   VARCHAR(500),
    rating  NUMERIC(7,2),
    updated_at  TIMESTAMP,
    discount_exp    TIMESTAMP,
    discount_start  TIMESTAMP,
    url_image   VARCHAR(255),
    category_id INT REFERENCES categories(id),
    brand_id    INT REFERENCES brands(id),
    supplier_id INT REFERENCES suppliers(id)
);

CREATE TABLE IF NOT EXISTS product_comments(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255),
    comment   VARCHAR(500),
    created_at  TIMESTAMP,
    product_id  INT REFERENCES products(id)
);