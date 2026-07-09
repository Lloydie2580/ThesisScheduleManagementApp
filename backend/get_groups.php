<?php
require_once "db.php";
require_once "helpers.php";

$adviser_id = $_GET["adviser_id"] ?? null;
if ($adviser_id) {
    $stmt = $pdo->prepare("
        SELECT sg.group_id, sg.group_code, sg.research_title, sg.adviser_id, u.full_name AS adviser_name
        FROM student_groups sg
        JOIN users u ON sg.adviser_id = u.user_id
        WHERE sg.adviser_id = ?
        ORDER BY sg.group_code
    ");
    $stmt->execute([$adviser_id]);
} else {
    $stmt = $pdo->query("
        SELECT sg.group_id, sg.group_code, sg.research_title, sg.adviser_id, u.full_name AS adviser_name
        FROM student_groups sg
        JOIN users u ON sg.adviser_id = u.user_id
        ORDER BY sg.group_code
    ");
}

$groups = [];
foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $group) {
    $member_stmt = $pdo->prepare("
        SELECT u.user_id, u.full_name, u.email, u.role
        FROM group_members gm
        JOIN users u ON gm.student_id = u.user_id
        WHERE gm.group_id = ?
    ");
    $member_stmt->execute([$group["group_id"]]);
    $group["group_id"] = (int)$group["group_id"];
    $group["adviser_id"] = (int)$group["adviser_id"];
    $group["members"] = $member_stmt->fetchAll(PDO::FETCH_ASSOC);
    $groups[] = $group;
}

send_response(true, "Groups loaded.", $groups);
?>
