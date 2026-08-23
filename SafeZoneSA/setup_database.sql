-- SafeZone SA Database Setup
-- Run this in phpMyAdmin or MySQL Workbench

CREATE DATABASE IF NOT EXISTS safezone_sa CHARACTER SET utf8 COLLATE utf8_general_ci;
USE safezone_sa;

CREATE TABLE IF NOT EXISTS users (
    userID       INT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    phone        VARCHAR(20),
    password     VARCHAR(255) NOT NULL,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS incidents (
    incidentID   INT AUTO_INCREMENT PRIMARY KEY,
    userID       INT NULL,
    type         VARCHAR(100) NOT NULL,
    description  TEXT NOT NULL,
    location     VARCHAR(255),
    status       ENUM('Under Review','Investigating','Resolved') DEFAULT 'Under Review',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE SET NULL
);

-- Sample data
INSERT INTO incidents (type, description, location, status) VALUES
('Robbery',             'Armed robbery at corner shop on Main St.',         'Johannesburg CBD', 'Under Review'),
('Vandalism',           'Graffiti on park walls near school entrance.',      'Soweto',           'Resolved'),
('Suspicious Activity', 'Unknown individuals casing vehicles on Oak Ave.',   'Sandton',          'Investigating'),
('Fire Hazard',         'Illegal burning in open lot near Bree Street.',     'Braamfontein',     'Under Review'),
('Road Accident',       'Minor collision at N1 on-ramp, no injuries.',       'Midrand',          'Resolved');

SELECT 'Database setup complete!' AS Status;
