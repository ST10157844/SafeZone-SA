<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    json_response(['success' => false, 'message' => 'Method not allowed.'], 405);
}

try {
    $sql = "
        SELECT
            i.incident_id AS incidentID,
            i.incident_type AS type,
            i.description,
            i.status,
            i.priority,
            i.submitted_at AS submittedAt,
            u.full_name AS reporterName,
            l.suburb,
            l.city
        FROM incidents i
        INNER JOIN users u ON u.user_id = i.reporter_user_id
        INNER JOIN locations l ON l.location_id = i.location_id
        ORDER BY i.submitted_at DESC
    ";

    $rows = db()->query($sql)->fetchAll();
    json_response($rows);
} catch (Throwable $e) {
    json_response(['success' => false, 'message' => 'Unable to retrieve incidents.'], 500);
}
