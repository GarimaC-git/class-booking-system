CREATE DATABASE IF NOT EXISTS class_booking;

USE class_booking;

CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at DATETIME
);

CREATE TABLE IF NOT EXISTS offerings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    teacher_id BIGINT NOT NULL,
    teacher_timezone VARCHAR(100) NOT NULL,
    course_id BIGINT NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    offering_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (offering_id) REFERENCES offerings(id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL,
    parent_timezone VARCHAR(100) NOT NULL,
    offering_id BIGINT NOT NULL,
    booked_at DATETIME,
    FOREIGN KEY (offering_id) REFERENCES offerings(id)
);