<?php
require_once "db.php";
require_once "helpers.php";

$stmt = $pdo->prepare("SELECT user_id, full_name, email, role FROM users WHERE role = 'Student' ORDER BY full_name");
$stmt->execute();
$students = $stmt->fetchAll(PDO::FETCH_ASSOC);

send_response(true, "Students retrieved.", $students);
