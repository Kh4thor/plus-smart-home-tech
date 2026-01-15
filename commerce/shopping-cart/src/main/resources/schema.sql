-- Таблица shopping_carts
CREATE TABLE shopping_carts (
    cart_id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL
);

-- Таблица cart_products для Map<UUID, Integer> products
CREATE TABLE cart_products (
    cart_id UUID NOT NULL REFERENCES shopping_carts(cart_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (cart_id, product_id)
);