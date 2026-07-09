<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["group_id", "research_title", "defense_date", "start_time", "end_time", "room_id", "adviser_id", "status"]);
$panelist_ids = $data["panelist_ids"] ?? [];
if (!is_array($panelist_ids) || count($panelist_ids) === 0) send_response(false, "At least one panelist is required.");
if (count($panelist_ids) > 2) send_response(false, "A maximum of 2 panelists can be selected.");
$defense_date = parse_date_input($data["defense_date"]);
$start_time = parse_time_input($data["start_time"]);
$end_time = parse_time_input($data["end_time"]);
if ($end_time <= $start_time) send_response(false, "End time must be after start time.");

$stmt = $pdo->prepare("SELECT adviser_id FROM student_groups WHERE group_id = ?");
$stmt->execute([$data["group_id"]]);
$group = $stmt->fetch(PDO::FETCH_ASSOC);
if (!$group || (int)$group["adviser_id"] !== (int)$data["adviser_id"]) {
    send_response(false, "Only the assigned adviser can create a schedule for this group.");
}

$conflict = schedule_conflict_message($pdo, $data["group_id"], $data["adviser_id"], $data["room_id"], $defense_date, $start_time, $end_time, $panelist_ids);
if ($conflict) send_response(false, $conflict);

$pdo->beginTransaction();
try {
    $stmt = $pdo->prepare("UPDATE student_groups SET research_title = ? WHERE group_id = ?");
    $stmt->execute([$data["research_title"], $data["group_id"]]);

    $stmt = $pdo->prepare("
        INSERT INTO defense_schedules (group_id, adviser_id, room_id, defense_date, start_time, end_time, status)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    ");
    $stmt->execute([$data["group_id"], $data["adviser_id"], $data["room_id"], $defense_date, $start_time, $end_time, $data["status"]]);
    $schedule_id = $pdo->lastInsertId();

    $stmt = $pdo->prepare("INSERT INTO schedule_panelists (schedule_id, professor_id) VALUES (?, ?)");
    foreach ($panelist_ids as $panelist_id) {
        $stmt->execute([$schedule_id, $panelist_id]);
    }

    notify_schedule_users($pdo, $schedule_id, "Defense schedule created", "A defense schedule has been created or assigned.");
    $pdo->commit();
    send_response(true, "Schedule created successfully.");
} catch (Exception $e) {
    $pdo->rollBack();
    send_response(false, "Unable to create schedule.");
}
?>
