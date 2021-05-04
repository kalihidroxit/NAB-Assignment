CREATE TABLE app_user(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name  VARCHAR(255),
    password VARCHAR(255),
    created_at    TIMESTAMP default now()
);

CREATE TABLE token_store(
    token   VARCHAR(500) PRIMARY KEY,
    user    VARCHAR(255),
    is_exp  BOOLEAN DEFAULT FALSE
);

CREATE TABLE third_party(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  url   VARCHAR(255),
  x_api_key VARCHAR(255)
);

CREATE TABLE history_search(
    id INT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(255),
    search_at   TIMESTAMP WITHOUT TIME ZONE,
    app_user_id INT REFERENCES app_user(id)
);

CREATE TABLE product_lookup(
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
    category    VARCHAR(255),
    brand    VARCHAR(255),
    supplier_name VARCHAR(255),
    supplier_info   VARCHAR(255),
    keyword VARCHAR(255),
    product_idf INT,
    third_party_id INT REFERENCES third_party(id),
    pulled_at   TIMESTAMP
)