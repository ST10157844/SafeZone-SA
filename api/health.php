<?php
declare(strict_types=1);

require_once __DIR__ . '/config.php';

try {
    $pdo = db();
    $pdo->query('SELECT 1');

    json_response([
        'success' => true,
        'api' => 'online',
        'database' => 'connected'
    ]);
} catch (Throwable $e) {
    json_response([
        'success' => false,
        'api' => 'online',
        'database' => 'disconnected',
        'message' => 'Database connection failed.'
    ], 500);
}
