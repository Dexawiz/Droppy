-- ===============================
-- PostgreSQL DDL for schema "droppy"
-- ===============================

CREATE SCHEMA IF NOT EXISTS droppy;
SET search_path TO droppy;

-- 2) Enum-types
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'category_enum') THEN
CREATE TYPE category_enum AS ENUM ('RESTAURANT', 'GROCERY', 'PHARMACY', 'OTHER');
END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role_enum') THEN
CREATE TYPE role_enum AS ENUM ('CUSTOMER', 'DRIVER', 'ADMIN');
END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'driver_status_enum') THEN
CREATE TYPE driver_status_enum AS ENUM ('OFFLINE', 'IDLE', 'ON_THE_WAY', 'DELIVERING', 'UNAVAILABLE');
END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'delivery_method_enum') THEN
CREATE TYPE delivery_method_enum AS ENUM ('CAR', 'BIKE', 'SCOOTER', 'FOOT');
END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_method_enum') THEN
CREATE TYPE payment_method_enum AS ENUM ('CASH', 'CARD', 'ONLINE');
END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_status_enum') THEN
CREATE TYPE order_status_enum AS ENUM ('NEW', 'ACCEPTED', 'IN_DELIVERY', 'DELIVERED', 'CANCELLED');
END IF;
END
$$;


-- companies
CREATE TABLE IF NOT EXISTS companies (
                                         id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                         name          VARCHAR(255) NOT NULL,
    address       VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(32),
    work_start    TIME,
    work_end      TIME,
    category      category_enum NOT NULL,
    CONSTRAINT uq_company_name_addr UNIQUE (name, address)
    );

-- users
CREATE TABLE IF NOT EXISTS users (
                                     id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     name             VARCHAR(100) NOT NULL,
    surname          VARCHAR(100) NOT NULL,
    role             role_enum NOT NULL,
    email            VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(32),
    card_number      VARCHAR(32),
    driver_status    driver_status_enum,
    delivery_method  delivery_method_enum,
    password_hash    VARCHAR(255) NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
    );

-- orders
CREATE TABLE IF NOT EXISTS orders (
                                      id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                      customer_id             BIGINT NOT NULL,
                                      driver_id               BIGINT,
                                      company_id              BIGINT NOT NULL,
                                      total_price             NUMERIC(10,2) NOT NULL,
    delivery_from_address   VARCHAR(255) NOT NULL,
    delivery_to_address     VARCHAR(255) NOT NULL,
    order_created_time      TIME NOT NULL,
    estimated_delivery_time TIME,
    payment_method          payment_method_enum NOT NULL,
    status                  order_status_enum NOT NULL,
    CONSTRAINT fk_orders_customer
    FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_orders_driver
    FOREIGN KEY (driver_id)   REFERENCES users(id),
    CONSTRAINT fk_orders_company
    FOREIGN KEY (company_id)  REFERENCES companies(id)
    );

-- products
CREATE TABLE IF NOT EXISTS products (
                                        id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       NUMERIC(10,2) NOT NULL,
    company_id  BIGINT NOT NULL,
    CONSTRAINT fk_products_company
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    CONSTRAINT uq_product_company_name UNIQUE (company_id, name)
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           order_id    BIGINT NOT NULL,
                                           product_id  BIGINT NOT NULL,
                                           quantity    INTEGER NOT NULL DEFAULT 1,
                                           price_each  NUMERIC(10,2) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_items_order
    FOREIGN KEY (order_id)  REFERENCES orders(id)   ON DELETE CASCADE,
    CONSTRAINT fk_items_product
    FOREIGN KEY (product_id) REFERENCES products(id)
    );

CREATE INDEX IF NOT EXISTS ix_orders_customer ON orders (customer_id);
CREATE INDEX IF NOT EXISTS ix_orders_driver   ON orders (driver_id);
CREATE INDEX IF NOT EXISTS ix_orders_company  ON orders (company_id);

CREATE INDEX IF NOT EXISTS ix_products_company ON products (company_id);
CREATE INDEX IF NOT EXISTS ix_items_product    ON order_items (product_id);
