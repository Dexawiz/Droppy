

SET search_path TO droppy, public;

BEGIN;

TRUNCATE order_items, orders, products, companies, users RESTART IDENTITY CASCADE;

INSERT INTO companies (name, address, phone_number, work_start, work_end, category) VALUES
                                                                                        ('Pizza Planet',       'Košice, Hlavná 1',      '+421900000100', '09:00', '22:00', 'RESTAURANT'),
                                                                                        ('Fresh Market',       'Košice, Trhová 12',     '+421900000200', '08:00', '20:00', 'GROCERY'),
                                                                                        ('MediHelp Pharmacy',  'Košice, Zdravotnícka 5','+421900000300', '09:00', '18:00', 'PHARMACY');
INSERT INTO users (name, surname, role, email, phone_number, password_hash) VALUES
                                                                                ('Admin', 'Root',        'ADMIN',    'admin@droppy.com',        '+421900000001', 'admin123'),
                                                                                ('Ivan',  'Petrov',      'CUSTOMER', 'ivan@droppy.com',         '+421900000002', 'pass123'),
                                                                                ('Anna',  'Kováč',       'CUSTOMER', 'anna@droppy.com',         '+421900000003', 'pass123');

INSERT INTO users (name, surname, role, email, phone_number, driver_status, delivery_method, password_hash) VALUES
                                                                                                                ('Peter', 'Novák',       'DRIVER',   'peter.driver@droppy.com', '+421900000004', 'OFFLINE',    'CAR',  'driver123'),
                                                                                                                ('Marek', 'Horváth',     'DRIVER',   'marek.driver@droppy.com', '+421900000005', 'OFFLINE', 'BIKE', 'driver123');

INSERT INTO products (name, description, price, company_id) VALUES
                                                                ('Margherita',     'Classic pizza',             7.90, (SELECT id FROM companies WHERE name='Pizza Planet')),
                                                                ('Pepperoni',      'Spicy pepperoni pizza',     8.90, (SELECT id FROM companies WHERE name='Pizza Planet')),
                                                                ('Avocado',        'Fresh avocado 1 pc',        1.20, (SELECT id FROM companies WHERE name='Fresh Market')),
                                                                ('Bananas 1kg',    'Yellow and sweet',          2.10, (SELECT id FROM companies WHERE name='Fresh Market')),
                                                                ('Paracetamol 500','Tablets 10 pcs',            3.50, (SELECT id FROM companies WHERE name='MediHelp Pharmacy'));

INSERT INTO orders (customer_id, driver_id, company_id, total_price,
                    delivery_from_address, delivery_to_address,
                    order_created_time, estimated_delivery_time,
                    payment_method, status)
VALUES (
           (SELECT id FROM users WHERE email='ivan@droppy.com'),
           (SELECT id FROM users WHERE email='peter.driver@droppy.com'),
           (SELECT id FROM companies WHERE name='Pizza Planet'),
           16.80,
           'Pizza Planet, Hlavná 1',
           'Košice, Watsonova 10',
           now() - interval '1 hour',
           now() + interval '30 minutes',
           'ONLINE', 'READY_FOR_PICKUP'
       );

INSERT INTO orders (customer_id, driver_id, company_id, total_price,
                    delivery_from_address, delivery_to_address,
                    order_created_time, estimated_delivery_time,
                    payment_method, status)
VALUES (
           (SELECT id FROM users WHERE email='anna@droppy.com'),
           (SELECT id FROM users WHERE email='marek.driver@droppy.com'),
           (SELECT id FROM companies WHERE name='Fresh Market'),
           3.30,
           'Fresh Market, Trhová 12',
           'Košice, Parková 2',
           now() - interval '2 hours',
           now() - interval '90 minutes',
           'CASH', 'DELIVERED'
       );

INSERT INTO order_items (order_id, product_id, quantity, price_each) VALUES
                                                                         ( (SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='ivan@droppy.com')  ORDER BY id DESC LIMIT 1),
                                                                         (SELECT id FROM products WHERE name='Margherita' AND company_id=(SELECT id FROM companies WHERE name='Pizza Planet')),
    1, 7.90),
 ( (SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='ivan@droppy.com')  ORDER BY id DESC LIMIT 1),
   (SELECT id FROM products WHERE name='Pepperoni'   AND company_id=(SELECT id FROM companies WHERE name='Pizza Planet')),
   1, 8.90),
 ( (SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='anna@droppy.com')  ORDER BY id DESC LIMIT 1),
   (SELECT id FROM products WHERE name='Avocado'     AND company_id=(SELECT id FROM companies WHERE name='Fresh Market')),
   1, 1.20),
 ( (SELECT id FROM orders WHERE customer_id=(SELECT id FROM users WHERE email='anna@droppy.com')  ORDER BY id DESC LIMIT 1),
   (SELECT id FROM products WHERE name='Bananas 1kg' AND company_id=(SELECT id FROM companies WHERE name='Fresh Market')),
   1, 2.10);

COMMIT;
