<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    json_response(['success' => false, 'message' => 'Method not allowed.'], 405);
}

$data = request_json();

$firebaseUid = trim((string)($data['firebaseUid'] ?? ''));
$type = trim((string)($data['type'] ?? ''));
$description = trim((string)($data['description'] ?? ''));
$locationId = (int)($data['locationId'] ?? 0);
$priority = strtolower(trim((string)($data['priority'] ?? 'medium')));

if ($firebaseUid === '' || $type === '' || $description === '' || $locationId <= 0) {
    json_response(['success' => false, 'message' => 'firebaseUid, type, description and a valid locationId are required.'], 422);
}

if (mb_strlen($description) < 10) {
    json_response(['success' => false, 'message' => 'Description must contain at least 10 characters.'], 422);
}

$allowedPriorities = ['low', 'medium', 'high', 'critical'];
if (!in_array($priority, $allowedPriorities, true)) {
    $priority = 'medium';
}

try {
    $pdo = db();

    $userStmt = $pdo->prepare('SELECT user_id FROM users WHERE firebase_uid = :firebase_uid LIMIT 1');
    $userStmt->execute([':firebase_uid' => $firebaseUid]);
    $userId = $userStmt->fetchColumn();

    if (!$userId) {
        json_response(['success' => false, 'message' => 'User is not synchronized with the SafeZone database.'], 404);
    }

    $locationStmt = $pdo->prepare('SELECT location_id FROM locations WHERE location_id = :location_id LIMIT 1');
    $locationStmt->execute([':location_id' => $locationId]);

    if (!$locationStmt->fetchColumn()) {
        json_response(['success' => false, 'message' => 'Selected location does not exist.'], 404);
    }

    $pdo->beginTransaction();

    $insert = $pdo->prepare(
        "INSERT INTO incidents
            (reporter_user_id, location_id, incident_type, description, status, priority)
         VALUES
            (:reporter_user_id, :location_id, :incident_type, :description, 'pending', :priority)"
    );

    $insert->execute([
        ':reporter_user_id' => (int)$userId,
        ':location_id' => $locationId,
        ':incident_type' => $type,
        ':description' => $description,
        ':priority' => $priority,
    ]);

    $incidentId = (int)$pdo->lastInsertId();

    $audit = $pdo->prepare(
        "INSERT INTO audit_logs
            (incident_id, performed_by_user_id, action, details)
         VALUES
            (:incident_id, :performed_by_user_id, 'INCIDENT_CREATED', :details)"
    );

    $audit->execute([
        ':incident_id' => $incidentId,
        ':performed_by_user_id' => (int)$userId,
        ':details' => "Incident {$incidentId} created through the SafeZone API."
    ]);

    $pdo->commit();

    json_response([
        'success' => true,
        'message' => 'Incident submitted successfully.',
        'incidentID' => $incidentId
    ], 201);

} catch (Throwable $e) {
    if (isset($pdo) && $pdo instanceof PDO && $pdo->inTransaction()) {
        $pdo->rollBack();
    }

    json_response(['success' => false, 'message' => 'Unable to submit incident.'], 500);
}
