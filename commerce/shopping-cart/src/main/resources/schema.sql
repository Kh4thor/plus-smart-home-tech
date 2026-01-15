CREATE TABLE shopping_carts (
    cart_id UUID PRIMARY KEY,
    username VARCHAR(255),
    status VARCHAR(50)
);

CREATE TABLE cart_products (
    cart_id UUID NOT NULL REFERENCES shopping_carts(cart_id) ON DELETE CASCADE,
    product_id UUID,
    quantity INTEGER,
    PRIMARY KEY (cart_id, product_id)
);