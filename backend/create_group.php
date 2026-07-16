<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["research_title", "adviser_id", "member_ids", "program"]);

$panelist_ids = $data["panelist_ids"] ?? [];
$year = date("Y");
$program = str_replace(' ', '', strtoupper($data["program"]));
$prefix = "$year$program";

// Check if a group with the same research title and adviser already exists (Basic debouncing/duplicate check)
$stmt = $pdo->prepare("SELECT group_id FROM student_groups WHERE research_title = ? AND adviser_id = ? LIMIT 1");
$stmt->execute([$data["research_title"], $data["adviser_id"]]);
if ($stmt->fetch()) {
    send_response(false, "A group with this thesis title already exists for this adviser.");
}

// Generate auto-incremented group code without spaces
$stmt = $pdo->prepare("SELECT COUNT(*) FROM student_groups WHERE group_code LIKE ?");
$stmt->execute([$prefix . "%"]);
$count = $stmt->fetchColumn();
$group_code = $prefix . str_pad($count + 1, 3, "0", STR_PAD_LEFT);

$pdo->beginTransaction();
try {
    $stmt = $pdo->prepare("INSERT INTO student_groups (group_code, research_title, adviser_id) VALUES (?, ?, ?)");
    $stmt->execute([$group_code, $data["research_title"], $data["adviser_id"]]);
    $group_id = $pdo->lastInsertId();

    $stmt = $pdo->prepare("INSERT INTO group_members (group_id, student_id) VALUES (?, ?)");
    foreach ($data["member_ids"] as $student_id) {
        $stmt->execute([$group_id, $student_id]);
    }

    try {
        $stmt = $pdo->prepare("INSERT INTO group_panelists (group_id, professor_id) VALUES (?, ?)");
        foreach ($panelist_ids as $professor_id) {
            $stmt->execute([$group_id, $professor_id]);
        }
    } catch (PDOException $e) {
        // Table group_panelists might not exist
    }

    $pdo->commit();
    send_response(true, "Group created successfully.", ["group_code" => $group_code]);
} catch (Exception $e) {
    $pdo->rollBack();
    send_response(false, "Unable to create group: " . $e->getMessage());
}
