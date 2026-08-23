<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);

if (empty($data['type']) || empty($data['description'])) {
    http_response_code(400);
    echo json_encode(["error" => "Type and description are required."]);
    exit();
}

try {
    $stmt = $conn->prepare(
        "INSERT INTO incidents (type, description, location, status, created_at)
         VALUES (:type, :description, :location, 'Under Review', NOW())"
    );
    $stmt->execute([
        ':type'        => $data['type'],
        ':description' => $data['description'],
        ':location'    => $data['location'] ?? ''
    ]);

    echo json_encode([
        "success"    => true,
        "incidentID" => $conn->lastInsertId(),
        "message"    => "Incident reported successfully."
    ]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
?>
