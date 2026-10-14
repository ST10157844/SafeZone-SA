<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    json_response(['success' => false, 'message' => 'Method not allowed.'], 405);
}

$data = request_json();

$firebaseUid = trim((string)($data['firebaseUid'] ?? ''));
$name = trim((string)($data['name'] ?? ''));
$email = trim((string)($data['email'] ?? ''));
$phone = trim((string)($data['phone'] ?? ''));

if ($firebaseUid === '' || $name === '' || $email === '') {
    json_response(['success' => false, 'message' => 'firebaseUid, name and email are required.'], 422);
}

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    json_response(['success' => false, 'message' => 'Invalid email address.'], 422);
}

try {
    $sql = "
        INSERT INTO users
            (firebase_uid, full_name, email, phone, role, status)
        VALUES
            (:firebase_uid, :full_name, :email, :phone, 'community', 'active')
        ON DUPLICATE KEY UPDATE
            full_name = VALUES(full_name),
            email = VALUES(email),
            phone = VALUES(phone),
            updated_at = CURRENT_TIMESTAMP
    ";

    $stmt = db()->prepare($sql);
    $stmt->execute([
        ':firebase_uid' => $firebaseUid,
        ':full_name' => $name,
        ':email' => $email,
        ':phone' => $phone !== '' ? $phone : null,
    ]);

    $lookup = db()->prepare(
        'SELECT user_id, firebase_uid, full_name, email, phone, role, status
         FROM users WHERE firebase_uid = :firebase_uid LIMIT 1'
    );
    $lookup->execute([':firebase_uid' => $firebaseUid]);
    $user = $lookup->fetch();

    json_response(['success' => true, 'message' => 'User synchronized.', 'user' => $user]);
} catch (Throwable $e) {
    json_response(['success' => false, 'message' => 'Unable to synchronize user.'], 500);
}
