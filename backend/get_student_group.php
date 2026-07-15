<?php
require_once "db.php";
require_once "helpers.php";

$student_id = $_GET["student_id"] ?? null;
if (!$student_id) send_response(false, "student_id is required.");

try {
    $stmt = $pdo->prepare("
        SELECT g.*, u.full_name as adviser_name
        FROM student_groups g
        JOIN group_members gm ON g.group_id = gm.group_id
        LEFT JOIN users u ON g.adviser_id = u.user_id
        WHERE gm.student_id = ?
        LIMIT 1
    ");
    $stmt->execute([$student_id]);
    $group = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($group) {
        $group_id = (int)$group["group_id"];
        $group["group_id"] = $group_id;
        $group["adviser_id"] = (int)$group["adviser_id"];

        // Get members
        $stmt = $pdo->prepare("
            SELECT u.user_id, u.full_name, u.email, u.role
            FROM group_members gm
            JOIN users u ON gm.student_id = u.user_id
            WHERE gm.group_id = ?
        ");
        $stmt->execute([$group_id]);
        $group["members"] = $stmt->fetchAll(PDO::FETCH_ASSOC);

        // Get panelists (Handle missing table gracefully)
        $group["panelists"] = [];
        try {
            $stmt = $pdo->prepare("
                SELECT u.user_id, u.full_name, u.email
                FROM group_panelists gp
                JOIN users u ON gp.professor_id = u.user_id
                WHERE gp.group_id = ?
            ");
            $stmt->execute([$group_id]);
            $group["panelists"] = $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            // Table group_panelists might not exist yet
        }

        send_response(true, "Group found.", $group);
    } else {
        send_response(false, "No group found for this student.");
    }
} catch (Exception $e) {
    send_response(false, "Error: " . $e->getMessage());
}
