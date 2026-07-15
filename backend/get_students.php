<?php
require_once "db.php";
require_once "helpers.php";

$stmt = $pdo->prepare("
    SELECT u.user_id, u.full_name, u.email, u.role
    FROM users u
    LEFT JOIN group_members gm ON u.user_id = gm.student_id
    WHERE LOWER(u.role) = 'student' AND gm.group_id IS NULL
    ORDER BY u.full_name
");
$stmt->execute();
$students = $stmt->fetchAll(PDO::FETCH_ASSOC);

send_response(true, "Available students retrieved.", $students);
