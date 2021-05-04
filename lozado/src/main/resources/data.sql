insert into APP_USER (USER_NAME, X_API_KEY) values ('Smart Choice', 'smart123456' );

INSERT INTO categories(NAME) VALUES ('Shoe');
INSERT INTO categories(NAME) VALUES ('Hat');
INSERT INTO categories(NAME) VALUES ('T-shirt');
INSERT INTO categories(NAME) VALUES ('Glove');

INSERT INTO suppliers(NAME, ADDRESS, ACCOUNT_NO, PHONE) VALUES ('SupA', 'Earth A', '164865356','016678945612');
INSERT INTO suppliers(NAME, ADDRESS, ACCOUNT_NO, PHONE) VALUES ('SupB', 'Earth B', '684135135','016678947345');
INSERT INTO suppliers(NAME, ADDRESS, ACCOUNT_NO, PHONE) VALUES ('SupC', 'Earth C', '6846532','01567754456');

INSERT INTO brands(NAME, COUNTRY) VALUES ('Adidas', 'Earth 1');
INSERT INTO brands(NAME, COUNTRY) VALUES ('Nike', 'Earth 2');
INSERT INTO brands(NAME, COUNTRY) VALUES ('LV', 'Earth 3');

INSERT INTO products(NAME, PRICE, DISCOUNT, PROMOTION, RATING, UPDATED_AT, DISCOUNT_EXP, DISCOUNT_START, CATEGORY_ID, BRAND_ID, SUPPLIER_ID, URL_IMAGE)
VALUES ('AXS Shoe', 100.00, 0, 'Buy 1 get 1 free', 5.0, now(), now(), now(), 1, 1, 1, 'image1');
INSERT INTO products(NAME, PRICE, DISCOUNT, PROMOTION, RATING, UPDATED_AT, DISCOUNT_EXP, DISCOUNT_START, CATEGORY_ID, BRAND_ID, SUPPLIER_ID, URL_IMAGE)
VALUES ('NXS Shoe', 150.00, 5, '', 5.0, now(), now(), now(), 1, 2, 3, 'image2');
INSERT INTO products(NAME, PRICE, DISCOUNT, PROMOTION, RATING, UPDATED_AT, DISCOUNT_EXP, DISCOUNT_START, CATEGORY_ID, BRAND_ID, SUPPLIER_ID, URL_IMAGE)
VALUES ('NXS Glove', 1000.00, 0, 'Buy 1 get 1 free', 5.0, now(), now(), now(), 4, 3, 2, 'image3');