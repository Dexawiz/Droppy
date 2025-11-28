SET search_path TO droppy, public;

BEGIN;

-- Очистка таблиц
TRUNCATE order_items, orders, products, companies, users, addresses RESTART IDENTITY CASCADE;

-- ===== Companies =====
INSERT INTO companies (name, address, phone_number, work_start, work_end, category) VALUES
                                                                                        ('Pizza Planet',      'Košice, Hlavná 1',      '+421900000100', '09:00', '22:00', 'RESTAURANT'),
                                                                                        ('Fresh Market',      'Košice, Trhová 12',     '+421900000200', '08:00', '20:00', 'GROCERY'),
                                                                                        ('MediHelp Pharmacy', 'Košice, Zdravotnícka 5', '+421900000300', '09:00', '18:00', 'PHARMACY');

-- ===== Users addresses  =====
INSERT INTO addresses (street, city, postal_code, country) VALUES
                                                               ('Košice, Watsonova 10', 'Košice', '04001', 'Slovakia'),
                                                               ('Košice, Parková 2',    'Košice', '04002', 'Slovakia'),
                                                               ('Košice, Trieda SNP 5', 'Košice', '04003', 'Slovakia'),
                                                               ('Košice, Hlavná 25',    'Košice', '04004', 'Slovakia');

-- ===== Users =====
INSERT INTO users (name, surname, role, email, phone_number, password_hash, address_id) VALUES
                                                                                            ('Admin', 'Root', 'ADMIN', 'admin@droppy.com', '+421900000001', 'admin123', NULL),
                                                                                            ('Ivan', 'Petrov', 'CUSTOMER', 'ivan@gmail.com', '+421900000002', 'pass123', 1),
                                                                                            ('Anna', 'Kováč', 'CUSTOMER', 'anna@gmail.com', '+421900000003', 'pass123', 2),
                                                                                            ('Juraj', 'Novotný', 'CUSTOMER', 'juraj@gmail.com', '+421900000006', 'pass123', 3),
                                                                                            ('Marta', 'Hrušková', 'CUSTOMER', 'marta@gmail.com', '+421900000007', 'pass123', 4);

INSERT INTO users (name, surname, role, email, phone_number, driver_status, delivery_method, password_hash, address_id) VALUES
                                                                                                                            ('Peter', 'Novák', 'DRIVER', 'peter.driver@droppy.com', '+421900000004', 'OFFLINE', 'CAR', 'driver123', NULL),
                                                                                                                            ('Marek', 'Horváth', 'DRIVER', 'marek.driver@droppy.com', '+421900000005', 'OFFLINE', 'BIKE', 'driver123', NULL),
                                                                                                                            ('Lucia', 'Farkašová', 'DRIVER', 'lucia.driver@droppy.com', '+421900000008', 'OFFLINE', 'CAR', 'driver123', NULL);

-- ===== Products =====
INSERT INTO products (name, description, price, company_id) VALUES
                                                                ('Margherita',      'Classic pizza',         7.90, (SELECT id FROM companies WHERE name='Pizza Planet')),
                                                                ('Pepperoni',       'Spicy pepperoni pizza', 8.90, (SELECT id FROM companies WHERE name='Pizza Planet')),
                                                                ('Hawaiian',        'Pizza with ham & pineapple', 9.50, (SELECT id FROM companies WHERE name='Pizza Planet')),
                                                                ('Avocado',         'Fresh avocado 1 pc',    1.20, (SELECT id FROM companies WHERE name='Fresh Market')),
                                                                ('Bananas 1kg',     'Yellow and sweet',      2.10, (SELECT id FROM companies WHERE name='Fresh Market')),
                                                                ('Apples 1kg',      'Fresh red apples',      2.50, (SELECT id FROM companies WHERE name='Fresh Market')),
                                                                ('Paracetamol 500', 'Tablets 10 pcs',        3.50, (SELECT id FROM companies WHERE name='MediHelp Pharmacy')),
                                                                ('Ibuprofen 200',   'Tablets 20 pcs',        4.20, (SELECT id FROM companies WHERE name='MediHelp Pharmacy'));

-- ===== Orders =====
INSERT INTO orders (customer_id, driver_id, company_id, total_price, delivery_from_address, delivery_to_address, order_created_time, estimated_delivery_time, payment_method, status)
VALUES
    ((SELECT id FROM users WHERE email='ivan@gmail.com'), NULL, (SELECT id FROM companies WHERE name='Pizza Planet'), 16.80, 'Pizza Planet, Hlavná 1', 'Košice, Watsonova 10', now() - interval '1 hour', now() + interval '30 minutes', 'ONLINE', 'PENDING'),
    ((SELECT id FROM users WHERE email='anna@gmail.com'), (SELECT id FROM users WHERE email='marek.driver@droppy.com'), (SELECT id FROM companies WHERE name='Fresh Market'), 3.30, 'Fresh Market, Trhová 12', 'Košice, Parková 2', now() - interval '2 hours', now() - interval '90 minutes', 'CASH', 'DELIVERED');


INSERT INTO orders (customer_id, driver_id, company_id, total_price, delivery_from_address, delivery_to_address, order_created_time, estimated_delivery_time, payment_method, status)
VALUES
    ((SELECT id FROM users WHERE email='juraj@gmail.com'), (SELECT id FROM users WHERE email='marek.driver@droppy.com'), (SELECT id FROM companies WHERE name='Pizza Planet'), 17.40, 'Pizza Planet, Hlavná 1', 'Košice, Trieda SNP 5', now() - interval '3 hours', now() + interval '45 minutes', 'ONLINE', 'ACCEPTED'),
    ((SELECT id FROM users WHERE email='marta@gmail.com'), (SELECT id FROM users WHERE email='peter.driver@droppy.com'), (SELECT id FROM companies WHERE name='MediHelp Pharmacy'), 7.70, 'MediHelp Pharmacy, Zdravotnícka 5', 'Košice, Hlavná 25', now() - interval '4 hours', now() - interval '30 minutes', 'CASH', 'DELIVERED');

-- ===== Order items =====
-- Ivan
INSERT INTO order_items (order_id, product_id, quantity, price_each) VALUES
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='ivan@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Margherita'), 1, 7.90),
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='ivan@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Pepperoni'), 1, 8.90);

-- Anna
INSERT INTO order_items (order_id, product_id, quantity, price_each) VALUES
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='anna@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Avocado'), 1, 1.20),
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='anna@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Bananas 1kg'), 1, 2.10);

-- Juraj
INSERT INTO order_items (order_id, product_id, quantity, price_each) VALUES
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='juraj@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Hawaiian'), 1, 9.50),
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='juraj@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Margherita'), 1, 7.90);

-- Marta
INSERT INTO order_items (order_id, product_id, quantity, price_each) VALUES
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='marta@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Paracetamol 500'), 2, 7.00),
                                                                         ((SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='marta@gmail.com') ORDER BY id DESC LIMIT 1), (SELECT id FROM products WHERE name='Ibuprofen 200'), 1, 4.20);

COMMIT;
