-- SafeZone SA - MySQL schema
-- XISD6329W Task 2
-- Designed for MySQL 8.x / Railway MySQL
-- IMPORTANT: This script drops the six SafeZone tables if they already exist.

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS incidents;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
    user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    firebase_uid VARCHAR(128) NOT NULL UNIQUE,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    phone VARCHAR(30) NULL,
    role ENUM('community','moderator','admin','authority') NOT NULL DEFAULT 'community',
    status ENUM('active','inactive','suspended') NOT NULL DEFAULT 'active',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_role (role),
    INDEX idx_users_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE locations (
    location_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    street_address VARCHAR(190) NOT NULL,
    suburb VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL DEFAULT 'Gauteng',
    latitude DECIMAL(10,7) NULL,
    longitude DECIMAL(10,7) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_locations_city_suburb (city, suburb)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE incidents (
    incident_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reporter_user_id INT UNSIGNED NOT NULL,
    location_id INT UNSIGNED NOT NULL,
    incident_type VARCHAR(80) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('pending','under_review','assigned','investigating','resolved','rejected') NOT NULL DEFAULT 'pending',
    priority ENUM('low','medium','high','critical') NOT NULL DEFAULT 'medium',
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_incidents_reporter
        FOREIGN KEY (reporter_user_id) REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_incidents_location
        FOREIGN KEY (location_id) REFERENCES locations(location_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    INDEX idx_incidents_reporter (reporter_user_id),
    INDEX idx_incidents_location (location_id),
    INDEX idx_incidents_status (status),
    INDEX idx_incidents_priority (priority),
    INDEX idx_incidents_submitted_at (submitted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE media (
    media_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    incident_id INT UNSIGNED NOT NULL,
    file_type ENUM('image','video') NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    original_file_name VARCHAR(255) NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_media_incident
        FOREIGN KEY (incident_id) REFERENCES incidents(incident_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    INDEX idx_media_incident (incident_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE assignments (
    assignment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    incident_id INT UNSIGNED NOT NULL,
    assigned_by_user_id INT UNSIGNED NOT NULL,
    assigned_to_user_id INT UNSIGNED NOT NULL,
    assignment_status ENUM('assigned','in_progress','completed','cancelled') NOT NULL DEFAULT 'assigned',
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL DEFAULT NULL,
    resolution_notes TEXT NULL,
    CONSTRAINT fk_assignments_incident
        FOREIGN KEY (incident_id) REFERENCES incidents(incident_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_assignments_assigned_by
        FOREIGN KEY (assigned_by_user_id) REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_assignments_assigned_to
        FOREIGN KEY (assigned_to_user_id) REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    INDEX idx_assignments_incident (incident_id),
    INDEX idx_assignments_assigned_to (assigned_to_user_id),
    INDEX idx_assignments_status (assignment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE audit_logs (
    log_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    incident_id INT UNSIGNED NULL,
    performed_by_user_id INT UNSIGNED NOT NULL,
    action VARCHAR(120) NOT NULL,
    details TEXT NULL,
    ip_address VARCHAR(45) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_incident
        FOREIGN KEY (incident_id) REFERENCES incidents(incident_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_audit_user
        FOREIGN KEY (performed_by_user_id) REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    INDEX idx_audit_incident (incident_id),
    INDEX idx_audit_user (performed_by_user_id),
    INDEX idx_audit_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
