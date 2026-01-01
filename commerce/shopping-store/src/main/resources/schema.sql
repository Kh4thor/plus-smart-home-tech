CREATE DATABASE shopping_store

CREATE TABLE products (
    product_id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    image_src VARCHAR(500),
    quantity_state VARCHAR(20) NOT NULL,
    product_state VARCHAR(20) NOT NULL,
    product_category VARCHAR(20),
    price INTEGER NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);