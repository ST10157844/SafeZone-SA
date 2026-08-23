CREATE DATABASE safezone_db;
USE safezone_db;

CREATE TABLE User (
userID INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
email VARCHAR(150),
passwordHash VARCHAR(255),
role VARCHAR(50),
status VARCHAR(20)
);

CREATE TABLE Incident (
incidentID INT AUTO_INCREMENT PRIMARY KEY,
userID INT,
type VARCHAR(50),
description TEXT,
status VARCHAR(20),
submittedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Location (
locationID INT AUTO_INCREMENT PRIMARY KEY,
incidentID INT,
address VARCHAR(255),
latitude DECIMAL(9,6),
longitude DECIMAL(9,6)
);

CREATE TABLE Media (
mediaID INT AUTO_INCREMENT PRIMARY KEY,
incidentID INT,
filePath VARCHAR(255),
fileType VARCHAR(20)
);

CREATE TABLE Assignment (
assignmentID INT AUTO_INCREMENT PRIMARY KEY,
incidentID INT,
assignedTo VARCHAR(100),
status VARCHAR(50)
);

CREATE TABLE AuditLog (
logID INT AUTO_INCREMENT PRIMARY KEY,
incidentID INT,
action VARCHAR(100),
actionDate DATETIME DEFAULT CURRENT_TIMESTAMP
);