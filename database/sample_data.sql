-- SafeZone SA - simulated sample data
-- At least 10 records are inserted into every core table.
-- No passwords are stored in this database.

INSERT INTO users
(user_id, firebase_uid, full_name, email, phone, role, status)
VALUES
(1, 'demo_uid_001', 'Thabo Mokoena', 'thabo.mokoena@example.com', '0710000001', 'community', 'active'),
(2, 'demo_uid_002', 'Naledi Molefe', 'naledi.molefe@example.com', '0710000002', 'community', 'active'),
(3, 'demo_uid_003', 'Kagiso Dlamini', 'kagiso.dlamini@example.com', '0710000003', 'community', 'active'),
(4, 'demo_uid_004', 'Lerato Nkosi', 'lerato.nkosi@example.com', '0710000004', 'community', 'active'),
(5, 'demo_uid_005', 'Sipho Ndlovu', 'sipho.ndlovu@example.com', '0710000005', 'community', 'active'),
(6, 'demo_uid_006', 'Amina Patel', 'amina.patel@example.com', '0710000006', 'moderator', 'active'),
(7, 'demo_uid_007', 'Mpho Sebeko', 'mpho.sebeko@example.com', '0710000007', 'moderator', 'active'),
(8, 'demo_uid_008', 'SafeZone Administrator', 'admin.safezone@example.com', '0710000008', 'admin', 'active'),
(9, 'demo_uid_009', 'CPF Response Officer One', 'cpf.officer1@example.com', '0710000009', 'authority', 'active'),
(10, 'demo_uid_010', 'CPF Response Officer Two', 'cpf.officer2@example.com', '0710000010', 'authority', 'active');

INSERT INTO locations
(location_id, street_address, suburb, city, province, latitude, longitude)
VALUES
(1, '100 Church Street', 'Pretoria Central', 'Pretoria', 'Gauteng', -25.7479000, 28.2293000),
(2, '22 Burnett Street', 'Hatfield', 'Pretoria', 'Gauteng', -25.7503000, 28.2379000),
(3, '45 Lynnwood Road', 'Brooklyn', 'Pretoria', 'Gauteng', -25.7670000, 28.2369000),
(4, '17 Steve Biko Road', 'Arcadia', 'Pretoria', 'Gauteng', -25.7448000, 28.2116000),
(5, '8 Atterbury Road', 'Menlo Park', 'Pretoria', 'Gauteng', -25.7706000, 28.2573000),
(6, '31 Justice Mahomed Street', 'Sunnyside', 'Pretoria', 'Gauteng', -25.7601000, 28.2115000),
(7, '12 Lavender Road', 'Wonderboom', 'Pretoria', 'Gauteng', -25.6918000, 28.2079000),
(8, '55 Soutpansberg Road', 'Riviera', 'Pretoria', 'Gauteng', -25.7246000, 28.2159000),
(9, '77 January Masilela Drive', 'Garsfontein', 'Pretoria', 'Gauteng', -25.7960000, 28.3050000),
(10, '14 WF Nkomo Street', 'Pretoria West', 'Pretoria', 'Gauteng', -25.7540000, 28.1670000);

INSERT INTO incidents
(incident_id, reporter_user_id, location_id, incident_type, description, status, priority, submitted_at)
VALUES
(1, 1, 1, 'Robbery', 'Simulated report of a robbery outside a convenience shop.', 'under_review', 'high', '2026-10-01 08:15:00'),
(2, 2, 2, 'Suspicious Activity', 'Unknown individuals were repeatedly checking parked vehicles.', 'investigating', 'medium', '2026-10-01 09:30:00'),
(3, 3, 3, 'Vandalism', 'Graffiti and damage were reported on a public wall.', 'resolved', 'low', '2026-10-02 12:05:00'),
(4, 4, 4, 'Theft', 'A simulated bicycle theft was reported near a residential entrance.', 'assigned', 'medium', '2026-10-03 18:20:00'),
(5, 5, 5, 'Vehicle Break-in', 'A parked vehicle window was damaged and property was reported missing.', 'investigating', 'high', '2026-10-04 21:10:00'),
(6, 1, 6, 'Streetlight Failure', 'Several streetlights were reported not working along the block.', 'pending', 'low', '2026-10-05 19:45:00'),
(7, 2, 7, 'Assault', 'A simulated assault incident was reported near a public transport stop.', 'assigned', 'critical', '2026-10-06 22:15:00'),
(8, 3, 8, 'Noise Disturbance', 'Persistent late-night noise disturbance was reported by residents.', 'rejected', 'low', '2026-10-07 23:05:00'),
(9, 4, 9, 'Burglary', 'Signs of forced entry were reported at a simulated residential property.', 'under_review', 'high', '2026-10-08 06:40:00'),
(10, 5, 10, 'Illegal Dumping', 'Waste was reported dumped in an unauthorised public area.', 'resolved', 'medium', '2026-10-09 14:25:00');

INSERT INTO media
(media_id, incident_id, file_type, file_url, original_file_name, uploaded_at)
VALUES
(1, 1, 'image', 'https://example.com/safezone/media/incident-001.jpg', 'incident-001.jpg', '2026-10-01 08:17:00'),
(2, 2, 'image', 'https://example.com/safezone/media/incident-002.jpg', 'incident-002.jpg', '2026-10-01 09:32:00'),
(3, 3, 'image', 'https://example.com/safezone/media/incident-003.jpg', 'incident-003.jpg', '2026-10-02 12:07:00'),
(4, 4, 'video', 'https://example.com/safezone/media/incident-004.mp4', 'incident-004.mp4', '2026-10-03 18:23:00'),
(5, 5, 'image', 'https://example.com/safezone/media/incident-005.jpg', 'incident-005.jpg', '2026-10-04 21:12:00'),
(6, 6, 'image', 'https://example.com/safezone/media/incident-006.jpg', 'incident-006.jpg', '2026-10-05 19:47:00'),
(7, 7, 'video', 'https://example.com/safezone/media/incident-007.mp4', 'incident-007.mp4', '2026-10-06 22:17:00'),
(8, 8, 'image', 'https://example.com/safezone/media/incident-008.jpg', 'incident-008.jpg', '2026-10-07 23:07:00'),
(9, 9, 'image', 'https://example.com/safezone/media/incident-009.jpg', 'incident-009.jpg', '2026-10-08 06:42:00'),
(10, 10, 'image', 'https://example.com/safezone/media/incident-010.jpg', 'incident-010.jpg', '2026-10-09 14:27:00');

INSERT INTO assignments
(assignment_id, incident_id, assigned_by_user_id, assigned_to_user_id, assignment_status, assigned_at, completed_at, resolution_notes)
VALUES
(1, 1, 6, 9, 'in_progress', '2026-10-01 08:45:00', NULL, 'Initial review completed; response follow-up in progress.'),
(2, 2, 7, 10, 'in_progress', '2026-10-01 10:00:00', NULL, 'Area patrol requested for verification.'),
(3, 3, 6, 9, 'completed', '2026-10-02 13:00:00', '2026-10-02 16:30:00', 'Damage documented and municipal maintenance notified.'),
(4, 4, 7, 10, 'assigned', '2026-10-03 18:50:00', NULL, 'Assigned for local follow-up.'),
(5, 5, 6, 9, 'in_progress', '2026-10-04 21:40:00', NULL, 'Vehicle incident under investigation.'),
(6, 6, 7, 10, 'assigned', '2026-10-05 20:00:00', NULL, 'Infrastructure fault forwarded for inspection.'),
(7, 7, 8, 9, 'in_progress', '2026-10-06 22:25:00', NULL, 'High-priority response assigned.'),
(8, 8, 6, 10, 'cancelled', '2026-10-07 23:20:00', NULL, 'Report rejected during moderation due to insufficient actionable detail.'),
(9, 9, 7, 9, 'assigned', '2026-10-08 07:10:00', NULL, 'Assigned for evidence review and follow-up.'),
(10, 10, 6, 10, 'completed', '2026-10-09 15:00:00', '2026-10-10 10:15:00', 'Dumping location cleared and case closed.');

INSERT INTO audit_logs
(log_id, incident_id, performed_by_user_id, action, details, ip_address, created_at)
VALUES
(1, 1, 1, 'INCIDENT_CREATED', 'Community member submitted incident 1.', '192.0.2.11', '2026-10-01 08:15:05'),
(2, 1, 6, 'STATUS_UPDATED', 'Moderator changed incident 1 to under_review.', '192.0.2.21', '2026-10-01 08:35:00'),
(3, 2, 7, 'ASSIGNMENT_CREATED', 'Incident 2 assigned to authority user 10.', '192.0.2.22', '2026-10-01 10:00:05'),
(4, 3, 9, 'INCIDENT_RESOLVED', 'Authority marked incident 3 as resolved.', '192.0.2.31', '2026-10-02 16:30:05'),
(5, 4, 4, 'MEDIA_UPLOADED', 'Reporter attached supporting media to incident 4.', '192.0.2.14', '2026-10-03 18:23:05'),
(6, 5, 6, 'STATUS_UPDATED', 'Moderator changed incident 5 to investigating.', '192.0.2.21', '2026-10-04 21:45:00'),
(7, 7, 8, 'PRIORITY_REVIEWED', 'Administrator confirmed critical priority for incident 7.', '192.0.2.25', '2026-10-06 22:24:00'),
(8, 8, 6, 'INCIDENT_REJECTED', 'Incident 8 rejected after moderation review.', '192.0.2.21', '2026-10-07 23:19:00'),
(9, 9, 7, 'STATUS_UPDATED', 'Incident 9 moved to under_review.', '192.0.2.22', '2026-10-08 07:00:00'),
(10, 10, 10, 'INCIDENT_RESOLVED', 'Authority completed incident 10 and recorded resolution.', '192.0.2.32', '2026-10-10 10:15:05');

SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users
UNION ALL
SELECT 'locations', COUNT(*) FROM locations
UNION ALL
SELECT 'incidents', COUNT(*) FROM incidents
UNION ALL
SELECT 'media', COUNT(*) FROM media
UNION ALL
SELECT 'assignments', COUNT(*) FROM assignments
UNION ALL
SELECT 'audit_logs', COUNT(*) FROM audit_logs;
