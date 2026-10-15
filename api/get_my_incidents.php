<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    json_response([
        'success' => false,
        'message' => 'Method not allowed.'
    ], 405);
}

$firebaseUid =
    trim((string)($_GET['firebaseUid'] ?? ''));

if ($firebaseUid === '') {
    json_response([
        'success' => false,
        'message' => 'firebaseUid is required.'
    ], 422);
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
            i.is_anonymous AS anonymous,

            u.full_name AS reporterName,

            l.street_address AS location,
            l.suburb,
            l.city

        FROM incidents i

        INNER JOIN users u
            ON u.user_id = i.reporter_user_id

        INNER JOIN locations l
            ON l.location_id = i.location_id

        WHERE u.firebase_uid = :firebase_uid

        ORDER BY i.submitted_at DESC
    ";

    $stmt = db()->prepare($sql);

    $stmt->execute([
        ':firebase_uid' => $firebaseUid
    ]);

    $incidents =
        $stmt->fetchAll();

    json_response($incidents);

} catch (Throwable $e) {

    json_response([
        'success' => false,
        'message' =>
            'Unable to retrieve user incidents.'
    ], 500);
}