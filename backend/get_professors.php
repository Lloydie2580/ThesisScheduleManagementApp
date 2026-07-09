<?php
require_once "db.php";
require_once "helpers.php";

$stmt = $pdo->prepare("SELECT user_id, full_name, email FROM users WHERE role = 'Professor' ORDER BY full_name");
$stmt->execute();
$professors = $stmt->fetchAll(PDO::FETCH_ASSOC);
foreach ($professors as &$professor) {
    $professor["user_id"] = (int)$professor["user_id"];
}

send_response(true, "Professors loaded.", $professors);
?>
