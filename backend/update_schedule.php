<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["schedule_id", "group_id", "research_title", "defense_date", "start_time", "end_time", "room_id", "adviser_id", "status"]);
$panelist_ids = $data["panelist_ids"] ?? [];
if (!is_array($panelist_ids) || count($panelist_ids) === 0) send_response(false, "At least one panelist is required.");
if (count($panelist_ids) > 2) send_response(false, "A maximum of 2 panelists can be selected.");
$defense_date = parse_date_input($data["defense_date"]);
$start_time = parse_time_input($data["start_time"]);
$end_time = parse_time_input($data["end_time"]);
if ($end_time <= $start_time) send_response(false, "End time must be after start time.");

$stmt = $pdo->prepare("SELECT adviser_id FROM defense_schedules WHERE schedule_id = ?");
$stmt->execute([$data["schedule_id"]]);
$schedule = $stmt->fetch(PDO::FETCH_ASSOC);
if (!$schedule || (int)$schedule["adviser_id"] !== (int)$data["adviser_id"]) {
    send_response(false, "Only the adviser can update this schedule.");
}

$conflict = schedule_conflict_message($pdo, $data["group_id"], $data["adviser_id"], $data["room_id"], $defense_date, $start_time, $end_time, $panelist_ids, $data["schedule_id"]);
if ($conflict) send_response(false, $conflict);

$pdo->beginTransaction();
try {
    $stmt = $pdo->prepare("UPDATE student_groups SET research_title = ? WHERE group_id = ?");
    $stmt->execute([$data["research_title"], $data["group_id"]]);

    $stmt = $pdo->prepare("
        UPDATE defense_schedules
        SET group_id = ?, room_id = ?, defense_date = ?, start_time = ?, end_time = ?, status = ?, updated_at = CURRENT_TIMESTAMP
        WHERE schedule_id = ?
    ");
    $stmt->execute([$data["group_id"], $data["room_id"], $defense_date, $start_time, $end_time, $data["status"], $data["schedule_id"]]);

    $stmt = $pdo->prepare("DELETE FROM schedule_panelists WHERE schedule_id = ?");
    $stmt->execute([$data["schedule_id"]]);
    $stmt = $pdo->prepare("INSERT INTO schedule_panelists (schedule_id, professor_id) VALUES (?, ?)");
    foreach ($panelist_ids as $panelist_id) {
        $stmt->execute([$data["schedule_id"], $panelist_id]);
    }

    notify_schedule_users($pdo, $data["schedule_id"], "Defense schedule updated", "A defense schedule has been updated.");
    $pdo->commit();
    send_response(true, "Schedule updated successfully.");
} catch (Exception $e) {
    $pdo->rollBack();
    send_response(false, "Unable to update schedule.");
}
?>
