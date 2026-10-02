-- Train Scheduling & Booking System DDL Schema

-- 1. Roles Lookup Table
CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. User-Roles Join Table (Many-to-Many)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- 4. Companion Profiles Table (1-to-Many with Users)
CREATE TABLE IF NOT EXISTS companions (
    companion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    nic_or_passport VARCHAR(30) NOT NULL,
    concession_type VARCHAR(50) NOT NULL DEFAULT 'NONE', -- 'STUDENT', 'SENIOR', 'NONE'
    concession_ref VARCHAR(50) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_companions_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. Admin Dynamic Portal Content (CMS Table)
CREATE TABLE IF NOT EXISTS portal_content (
    content_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_key VARCHAR(100) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    content_value TEXT NOT NULL,
    category VARCHAR(50) NOT NULL DEFAULT 'LANDING_PAGE',
    updated_by BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_portal_content_user FOREIGN KEY (updated_by) REFERENCES users(user_id)
);

-- 6. Train Schedules & Routes
CREATE TABLE IF NOT EXISTS train_schedules (
    schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_number VARCHAR(20) NOT NULL,
    train_name VARCHAR(100) NOT NULL,
    origin_station VARCHAR(100) NOT NULL,
    destination_station VARCHAR(100) NOT NULL,
    departure_time VARCHAR(20) NOT NULL,
    arrival_time VARCHAR(20) NOT NULL,
    travel_date VARCHAR(20) NOT NULL,
    total_seats INT NOT NULL DEFAULT 120,
    available_seats INT NOT NULL DEFAULT 120,
    base_fare DECIMAL(10, 2) NOT NULL DEFAULT 500.00,
    platform_number VARCHAR(10) NOT NULL DEFAULT '1',
    status VARCHAR(30) NOT NULL DEFAULT 'ON_TIME', -- 'ON_TIME', 'DELAYED', 'BOARDING', 'DEPARTED', 'CANCELLED'
    delay_minutes INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Bookings Table (1-to-Many with Users)
CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_reference VARCHAR(30) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    travel_date VARCHAR(20) NOT NULL,
    passenger_count INT NOT NULL DEFAULT 1,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    final_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED', -- 'CONFIRMED', 'CANCELLED', 'COMPLETED', 'REFUND_PENDING'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_schedule FOREIGN KEY (schedule_id) REFERENCES train_schedules(schedule_id)
);

-- 8. Booking Passengers (1-to-Many with Bookings)
CREATE TABLE IF NOT EXISTS booking_passengers (
    passenger_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    nic_or_passport VARCHAR(30) NOT NULL,
    concession_type VARCHAR(50) NOT NULL DEFAULT 'NONE',
    fare_applied DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_booking_passengers_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- 9. Support & Refund Tickets
CREATE TABLE IF NOT EXISTS support_tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(30) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    booking_id BIGINT NULL,
    category VARCHAR(50) NOT NULL, -- 'REFUND', 'QUERY', 'COMPLAINT'
    subject VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- 'PENDING', 'APPROVED', 'REJECTED', 'RESOLVED'
    resolution_notes TEXT NULL,
    resolved_by BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_support_tickets_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_support_tickets_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL,
    CONSTRAINT fk_support_tickets_resolver FOREIGN KEY (resolved_by) REFERENCES users(user_id)
);

-- 10. Financial Transactions
CREATE TABLE IF NOT EXISTS financial_transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_ref VARCHAR(40) NOT NULL UNIQUE,
    booking_id BIGINT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_type VARCHAR(30) NOT NULL, -- 'PAYMENT', 'REFUND'
    payment_method VARCHAR(50) NOT NULL DEFAULT 'CREDIT_CARD',
    status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_financial_transactions_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL
);
