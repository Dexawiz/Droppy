-- Create schema and use it
CREATE SCHEMA IF NOT EXISTS droppy;
SET search_path TO droppy;

-- ===== ENUM types (PostgreSQL) =====
CREATE TYPE company_category_enum AS ENUM ('PIZZA',
    'SUSHI',
    'DESSERT',
    'DRINKS',
    'RESTAURANT',
    'GROCERY',
    'PHARMACY',
    'OTHER'
    );

CREATE TYPE user_role_enum        AS ENUM ('CUSTOMER', 'DRIVER', 'ADMIN');
CREATE TYPE driver_status_enum    AS ENUM (
    'OFFLINE',
    'ONLINE',
    'AVAILABLE',
    'EN_ROUTE_TO_PICKUP_ORDER',
    'ARRIVED_AT_PICKUP',
    'EN_ROUTE_TO_CLIENT',
    'ARRIVED_TO_CLIENT',
    'BANNED');
CREATE TYPE delivery_method_enum  AS ENUM ('CAR', 'BIKE', 'SCOOTER');

CREATE TYPE payment_method_enum   AS ENUM ('CASH', 'ONLINE');
CREATE TYPE order_status_enum     AS ENUM (
    'PENDING',
    'ACCEPTED',
    'IN_PREPARATION',
    'READY_FOR_PICKUP',
    'PICKED_UP',
    'DELIVERED',
    'CANCELLED',
    'COMPLETED'
);


-- ===== companies =====
CREATE TABLE companies (
                           id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           name         VARCHAR(255) NOT NULL,
                           address      VARCHAR(255) NOT NULL,
                           phone_number VARCHAR(32),
                           work_start   TIME,
                           work_end     TIME,
                           category     company_category_enum NOT NULL,
                           CONSTRAINT uq_company_name_addr UNIQUE (name, address)
);

-- ===== users =====
CREATE TABLE users (
                       id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name            VARCHAR(100) NOT NULL,
                       surname         VARCHAR(100) NOT NULL,
                       role            user_role_enum NOT NULL,
                       email           VARCHAR(255) NOT NULL,
                       phone_number    VARCHAR(32),
                       card_number     VARCHAR(32),
                       driver_status   driver_status_enum,
                       delivery_method delivery_method_enum,
                       password_hash   VARCHAR(255) NOT NULL,
                       CONSTRAINT uq_users_email UNIQUE (email)
);

-- ===== orders =====
CREATE TABLE orders (
                        id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        customer_id             BIGINT NOT NULL,
                        driver_id               BIGINT,
                        company_id              BIGINT NOT NULL,
                        total_price             NUMERIC(10,2) NOT NULL,
                        delivery_from_address   VARCHAR(255) NOT NULL,
                        delivery_to_address     VARCHAR(255) NOT NULL,
                        order_created_time      TIMESTAMP NOT NULL,
                        estimated_delivery_time TIMESTAMP,
                        payment_method          payment_method_enum NOT NULL,
                        status                  order_status_enum NOT NULL,
                        CONSTRAINT fk_orders_company   FOREIGN KEY (company_id)  REFERENCES companies(id),
                        CONSTRAINT fk_orders_customer  FOREIGN KEY (customer_id) REFERENCES users(id),
                        CONSTRAINT fk_orders_driver    FOREIGN KEY (driver_id)   REFERENCES users(id)
);

CREATE INDEX ix_orders_customer ON orders(customer_id);
CREATE INDEX ix_orders_driver   ON orders(driver_id);
CREATE INDEX ix_orders_company  ON orders(company_id);

-- ===== products =====
CREATE TABLE products (
                          id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name        VARCHAR(255) NOT NULL,
                          description TEXT,
                          price       NUMERIC(10,2) NOT NULL,
                          company_id  BIGINT NOT NULL,
                          CONSTRAINT uq_product_company_name UNIQUE (company_id, name),
                          CONSTRAINT fk_products_company FOREIGN KEY (company_id)
                              REFERENCES companies(id) ON DELETE CASCADE
);

CREATE INDEX ix_products_company ON products(company_id);

-- ===== order_items =====
CREATE TABLE order_items (
                             order_id   BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             quantity   INT    NOT NULL DEFAULT 1,
                             price_each NUMERIC(10,2) NOT NULL,
                             PRIMARY KEY (order_id, product_id),
                             CONSTRAINT fk_items_order   FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
                             CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX ix_items_product ON order_items(product_id);
