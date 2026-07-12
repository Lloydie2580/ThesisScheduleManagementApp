<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["group_id", "research_title", "defense_date", "start_time", "end_time", "room_id", "adviser_id", "status", "requester_id"]);

$panelist_ids = $data["panelist_ids"] ?? [];
if (!is_array($panelist_ids) || count($panelist_ids) === 0) send_response(false, "At least one panelist is required.");
if (count($panelist_ids) > 2) send_response(false, "A maximum of 2 panelists can be selected.");

$defense_date = parse_date_input($data["defense_date"]);
$start_time = parse_time_input($data["start_time"]);
$end_time = parse_time_input($data["end_time"]);
if ($end_time <= $start_time) send_response(false, "End time must be after start time.");

// Authorization check
$group_id = $data["group_id"];
$requester_id = $data["requester_id"];
$requester_role = strtolower($data["requester_role"] ?? "");

$stmt = $pdo->prepare("SELECT adviser_id FROM student_groups WHERE group_id = ?");
$stmt->execute([$group_id]);
$group = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$group) send_response(false, "Group not found.");

$actual_adviser_id = (int)$group["adviser_id"];
$is_authorized = false;

if ($requester_role === "professor") {
    if ($requester_id == $actual_adviser_id) {
        $is_authorized = true;
    } else {
        send_response(false, "Only the assigned adviser can create a schedule for this group.");
    }
} else if ($requester_role === "student") {
    $stmt = $pdo->prepare("SELECT 1 FROM group_members WHERE group_id = ? AND student_id = ?");
    $stmt->execute([$group_id, $requester_id]);
    if ($stmt->fetch()) {
        $is_authorized = true;
    } else {
        send_response(false, "You must be a member of the group to request a schedule.");
    }
}

if (!$is_authorized) send_response(false, "Unauthorized request.");

$conflict = schedule_conflict_message($pdo, $group_id, $actual_adviser_id, $data["room_id"], $defense_date, $start_time, $end_time, $panelist_ids);
if ($conflict) send_response(false, $conflict);

$adviser_approved = ($requester_role === "professor" && $requester_id == $actual_adviser_id) ? 1 : 0;

$pdo->beginTransaction();
try {
    $stmt = $pdo->prepare("UPDATE student_groups SET research_title = ? WHERE group_id = ?");
    $stmt->execute([$data["research_title"], $group_id]);

    $stmt = $pdo->prepare("
        INSERT INTO defense_schedules (group_id, adviser_id, room_id, defense_date, start_time, end_time, status, adviser_approved)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    ");
    $stmt->execute([$group_id, $actual_adviser_id, $data["room_id"], $defense_date, $start_time, $end_time, $data["status"], $adviser_approved]);
    $schedule_id = $pdo->lastInsertId();

    $stmt = $pdo->prepare("INSERT INTO schedule_panelists (schedule_id, professor_id) VALUES (?, ?)");
    foreach ($panelist_ids as $panelist_id) {
        $stmt->execute([$schedule_id, $panelist_id]);
    }

    $title = ($requester_role === "student") ? "Schedule Request" : "Defense Schedule Created";
    $message = ($requester_role === "student") ? "A student has requested a defense schedule." : "A defense schedule has been created by the adviser.";
    notify_schedule_users($pdo, $schedule_id, $title, $message);

    $pdo->commit();
    send_response(true, "Schedule " . (($requester_role === "student") ? "requested" : "created") . " successfully.");
} catch (Exception $e) {
    $pdo->rollBack();
    send_response(false, "Unable to create schedule: " . $e->getMessage());
}
