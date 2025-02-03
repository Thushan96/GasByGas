-- Create and insert sample data for Role
INSERT INTO role (id, name) VALUES
                                (1, 'ROLE_ADMIN'),
                                (2, 'ROLE_USER'),
                                (3, 'ROLE_MANAGER');

-- Insert data for User (password will be null initially, hashed in the application)
INSERT INTO user (id, name, contact_no, password) VALUES
    (6, 'saman', 1234567890, NULL);

-- Insert User-Role mapping
INSERT INTO user_roles (user_id, role_id) VALUES
    (6, 2); -- Assign ROLE_USER to "saman"

-- Insert sample data for Gas
INSERT INTO gas (id, capacity, price, stock) VALUES
                                                 (1, 5, 1000, 50),
                                                 (2, 10, 1800, 30),
                                                 (3, 15, 2500, 20);

-- Insert sample data for Outlet
INSERT INTO outlet (id, name, location) VALUES
                                            (1, 'Main Outlet', 'City Center'),
                                            (2, 'Suburban Outlet', 'Westside');

-- Insert sample data for OutletStock
INSERT INTO outlet_stock (outlet_id, gas_id, quantity) VALUES
                                                               ( 1, 1, 20),
                                                               (1, 2, 15),
                                                               (2, 3, 10);

-- Insert sample data for Order
INSERT INTO orders (id, user_id, outlet_id, status) VALUES
                                                        (1, 6, 1, 'Requested'),
                                                        (2, 6, 2, 'Requested');

-- Insert sample data for OrderGas
INSERT INTO order_gas (id, order_id, gas_id, quantity) VALUES
                                                           (1, 1, 1, 5),
                                                           (2, 1, 2, 2),
                                                           (3, 2, 3, 1);

-- Insert sample data for DeliverySchedule
INSERT INTO delivery_schedule (id, outlet_id, delivery_date, status, delivered, next_delivery_date) VALUES
                                                                                                           (1, 1, '2025-01-01', 'Pending', FALSE, '2025-01-15'),
                                                                                                           (2, 2, '2025-01-02', 'Pending', FALSE, '2025-01-16');

-- Insert sample data for DeliveryScheduleGas
INSERT INTO delivery_schedule_gas (id, delivery_schedule_id, gas_id, quantity) VALUES
                                                                                   (1, 1, 1, 10),
                                                                                   (2, 1, 2, 5),
                                                                                   (3, 2, 3, 8);
