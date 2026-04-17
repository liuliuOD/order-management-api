-- 1. Product Categories
CREATE TABLE product_categories (
    category_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_name VARCHAR(100) NOT NULL,
    tax_rate DECIMAL(5,4) NOT NULL,
    calculation_type VARCHAR(32) NOT NULL DEFAULT 'DEFAULT',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_calculation_type CHECK (calculation_type IN ('DEFAULT', 'ELECTRONICS', 'FOOD'))
);

-- 2. Users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ
);

-- 3. Products
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_category_id UUID NOT NULL, -- Logical reference
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(19,4) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Orders
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,    -- Logical reference
    product_id UUID NOT NULL, -- Logical reference
    order_amount INTEGER NOT NULL CHECK (order_amount > 0),
    unit_price_snapshot DECIMAL(19,4) NOT NULL,
    tax_rate_snapshot DECIMAL(5,4) NOT NULL,
    total_cost DECIMAL(19,4) NOT NULL,
    status VARCHAR(32) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT chk_order_status CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'CANCELLED'))
);

-- Indexes
CREATE INDEX idx_products_category ON products(product_category_id);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_product ON orders(product_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_users_email ON users(email) WHERE is_deleted = FALSE;
