<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    json_response([
        'success' => false,
        'message' => 'Method not allowed.'
    ], 405);
}

$data = request_json();

$firebaseUid =
    trim((string)($data['firebaseUid'] ?? ''));

$type =
    trim((string)($data['type'] ?? ''));

$description =
    trim((string)($data['description'] ?? ''));

$location =
    trim((string)($data['location'] ?? ''));

$priority =
    strtolower(trim(
        (string)($data['priority'] ?? 'medium')
    ));

$anonymous =
    (bool)($data['anonymous'] ?? false);


if (
    $firebaseUid === '' ||
    $type === '' ||
    $description === '' ||
    $location === ''
) {

    json_response([
        'success' => false,
        'message' =>
            'Type, description and location are required.'
    ], 422);
}


if (mb_strlen($description) < 10) {

    json_response([
        'success' => false,
        'message' =>
            'Description must contain at least 10 characters.'
    ], 422);
}


if (mb_strlen($description) > 500) {

    json_response([
        'success' => false,
        'message' =>
            'Description cannot exceed 500 characters.'
    ], 422);
}


$allowedPriorities = [
    'low',
    'medium',
    'high',
    'critical'
];

if (
    !in_array(
        $priority,
        $allowedPriorities,
        true
    )
) {
    $priority = 'medium';
}


try {

    $pdo = db();


    // Find the currently authenticated SafeZone user.
    $userStmt = $pdo->prepare(
        'SELECT user_id
         FROM users
         WHERE firebase_uid = :firebase_uid
         LIMIT 1'
    );

    $userStmt->execute([
        ':firebase_uid' => $firebaseUid
    ]);

    $userId =
        $userStmt->fetchColumn();


    if (!$userId) {

        json_response([
            'success' => false,
            'message' =>
                'User is not synchronized with SafeZone.'
        ], 404);
    }


    $pdo->beginTransaction();


    /*
     * Save the location supplied by the user.
     *
     * We are not inventing suburb/city values when
     * the user has not supplied them.
     */
    $locationStmt = $pdo->prepare(
        "INSERT INTO locations
            (
                street_address,
                suburb,
                city,
                province
            )
         VALUES
            (
                :street_address,
                'Not specified',
                'Not specified',
                'Not specified'
            )"
    );

    $locationStmt->execute([
        ':street_address' => $location
    ]);

    $locationId =
        (int)$pdo->lastInsertId();


    // Create the actual incident.
    $incidentStmt = $pdo->prepare(
        "INSERT INTO incidents
            (
                reporter_user_id,
                location_id,
                incident_type,
                description,
                status,
                priority,
                is_anonymous
            )
         VALUES
            (
                :reporter_user_id,
                :location_id,
                :incident_type,
                :description,
                'pending',
                :priority,
                :is_anonymous
            )"
    );


    $incidentStmt->execute([
        ':reporter_user_id' =>
            (int)$userId,

        ':location_id' =>
            $locationId,

        ':incident_type' =>
            $type,

        ':description' =>
            $description,

        ':priority' =>
            $priority,

        ':is_anonymous' =>
            $anonymous ? 1 : 0
    ]);


    $incidentId =
        (int)$pdo->lastInsertId();


    // Record the action for accountability.
    $auditStmt = $pdo->prepare(
        "INSERT INTO audit_logs
            (
                incident_id,
                performed_by_user_id,
                action,
                details
            )
         VALUES
            (
                :incident_id,
                :user_id,
                'INCIDENT_CREATED',
                :details
            )"
    );


    $auditStmt->execute([
        ':incident_id' =>
            $incidentId,

        ':user_id' =>
            (int)$userId,

        ':details' =>
            "Incident {$incidentId} submitted through the SafeZone Android application."
    ]);


    $pdo->commit();


    json_response([
        'success' => true,
        'message' =>
            'Incident submitted successfully.',
        'incidentID' =>
            $incidentId
    ], 201);


} catch (Throwable $e) {

    if (
        isset($pdo) &&
        $pdo instanceof PDO &&
        $pdo->inTransaction()
    ) {
        $pdo->rollBack();
    }


    json_response([
        'success' => false,
        'message' =>
            'Unable to submit incident.'
    ], 500);
}