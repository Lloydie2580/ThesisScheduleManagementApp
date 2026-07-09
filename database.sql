CREATE DATABASE IF NOT EXISTS thesis_schedule_db;
USE thesis_schedule_db;

DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS schedule_panelists;
DROP TABLE IF EXISTS defense_schedules;
DROP TABLE IF EXISTS group_members;
DROP TABLE IF EXISTS student_groups;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Student', 'Professor') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_groups (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    group_code VARCHAR(30) NOT NULL UNIQUE,
    research_title VARCHAR(255) NOT NULL,
    adviser_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_groups_adviser FOREIGN KEY (adviser_id) REFERENCES users(user_id)
);

CREATE TABLE group_members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    student_id INT NOT NULL,
    CONSTRAINT fk_members_group FOREIGN KEY (group_id) REFERENCES student_groups(group_id) ON DELETE CASCADE,
    CONSTRAINT fk_members_student FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE defense_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    adviser_id INT NOT NULL,
    room_id INT NOT NULL,
    defense_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status ENUM('Pending', 'Scheduled', 'Rescheduled', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedules_group FOREIGN KEY (group_id) REFERENCES student_groups(group_id),
    CONSTRAINT fk_schedules_adviser FOREIGN KEY (adviser_id) REFERENCES users(user_id),
    CONSTRAINT fk_schedules_room FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE schedule_panelists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id INT NOT NULL,
    professor_id INT NOT NULL,
    CONSTRAINT fk_panelists_schedule FOREIGN KEY (schedule_id) REFERENCES defense_schedules(schedule_id) ON DELETE CASCADE,
    CONSTRAINT fk_panelists_professor FOREIGN KEY (professor_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(120) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_schedule_room_time ON defense_schedules(room_id, defense_date, start_time, end_time);
CREATE INDEX idx_schedule_adviser_time ON defense_schedules(adviser_id, defense_date, start_time, end_time);
CREATE INDEX idx_schedule_group ON defense_schedules(group_id);

INSERT INTO rooms (room_name) VALUES
('R407'),
('E515'),
('Auditorium'),
('Cafe Enrique');

-- Seed password for all sample users: password
INSERT INTO users (full_name, email, password_hash, role) VALUES
('Lloydie F. Keikeu', 'student@example.com', '$2y$10$pwUBWNqRPNBMFlKAgFsC9.VzZvJOQGRmouKvNe74/d8x7tFpoIXa2', 'Student'),
('Dennis A. Martillano', 'adviser@example.com', '$2y$10$pwUBWNqRPNBMFlKAgFsC9.VzZvJOQGRmouKvNe74/d8x7tFpoIXa2', 'Professor'),
('Jonalyn G. Ebron', 'panelist@example.com', '$2y$10$pwUBWNqRPNBMFlKAgFsC9.VzZvJOQGRmouKvNe74/d8x7tFpoIXa2', 'Professor'),
('Aurelia Sharlene O. Delos Santos', 'panelist2@example.com', '$2y$10$pwUBWNqRPNBMFlKAgFsC9.VzZvJOQGRmouKvNe74/d8x7tFpoIXa2', 'Professor');

INSERT INTO student_groups (group_code, research_title, adviser_id) VALUES
('2023CS001', 'Thesis and Capstone Defense Schedule Management Application', 2);

INSERT INTO group_members (group_id, student_id) VALUES (1, 1);
