<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["schedule_id", "professor_id"]);

$schedule_id = $data["schedule_id"];
$professor_id = $data["professor_id"];

$stmt = $pdo->prepare("SELECT * FROM defense_schedules WHERE schedule_id = ?");
$stmt->execute([$schedule_id]);
$schedule = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$schedule) send_response(false, "Schedule not found.");

$is_adviser = (int)$schedule["adviser_id"] === (int)$professor_id;
$stmt = $pdo->prepare("SELECT 1 FROM schedule_panelists WHERE schedule_id = ? AND professor_id = ?");
$stmt->execute([$schedule_id, $professor_id]);
$is_panelist = (bool)$stmt->fetch();

if (!$is_adviser && !$is_panelist) {
    send_response(false, "Unauthorized action.");
}

$stmt = $pdo->prepare("UPDATE defense_schedules SET status = 'Cancelled' WHERE schedule_id = ?");
if ($stmt->execute([$schedule_id])) {
    notify_schedule_users($pdo, $schedule_id, "Schedule Rejected", "The defense schedule has been rejected.");
    send_response(true, "Schedule rejected successfully.");
} else {
    send_response(false, "Failed to reject schedule.");
}
?>
