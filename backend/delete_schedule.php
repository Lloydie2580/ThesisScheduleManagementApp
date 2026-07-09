<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["schedule_id", "professor_id"]);

$stmt = $pdo->prepare("SELECT adviser_id FROM defense_schedules WHERE schedule_id = ?");
$stmt->execute([$data["schedule_id"]]);
$schedule = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$schedule) {
    send_response(false, "Schedule not found.");
}

if ((int)$schedule["adviser_id"] !== (int)$data["professor_id"]) {
    send_response(false, "Only the adviser can delete this schedule.");
}

$stmt = $pdo->prepare("DELETE FROM defense_schedules WHERE schedule_id = ?");
$stmt->execute([$data["schedule_id"]]);

send_response(true, "Schedule deleted successfully.");
?>
