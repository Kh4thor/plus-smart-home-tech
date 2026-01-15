CREATE TABLE IF NOT EXISTS addresses (
    address_id UUID PRIMARY KEY,
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    house VARCHAR(255),
    flat VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS dimensions (
    dimension_id UUID PRIMARY KEY,
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS warehouse_products (
    product_id UUID PRIMARY KEY,
    quantity INTEGER,
    weight DOUBLE PRECISION,
    fragile BOOLEAN,
    dimension_id UUID UNIQUE,
    FOREIGN KEY (dimension_id) REFERENCES dimensions(dimension_id)
);

CREATE INDEX IF NOT EXISTS idx_addresses_city ON addresses(city);
CREATE INDEX IF NOT EXISTS idx_warehouse_products_fragile ON warehouse_products(fragile);