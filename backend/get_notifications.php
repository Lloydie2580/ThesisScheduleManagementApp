<?php
require_once "db.php";
require_once "helpers.php";

$user_id = $_GET["user_id"] ?? "";
if ($user_id === "") send_response(false, "user_id is required.");

$stmt = $pdo->prepare("
    SELECT notification_id, title, message, created_at, is_read
    FROM notifications
    WHERE user_id = ?
    ORDER BY created_at DESC
");
$stmt->execute([$user_id]);
$items = $stmt->fetchAll(PDO::FETCH_ASSOC);
foreach ($items as &$item) {
    $item["notification_id"] = (int)$item["notification_id"];
    $item["is_read"] = (int)$item["is_read"];
}

send_response(true, "Notifications loaded.", $items);
?>
