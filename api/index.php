<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

json_response([
    'success' => true,
    'service' => 'SafeZone SA API',
    'version' => '1.0',
    'endpoints' => [
        'GET /health.php',
        'GET /get_incidents.php',
        'POST /sync_user.php',
        'POST /post_incident.php'
    ]
]);
