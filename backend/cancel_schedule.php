<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["schedule_id", "professor_id"]);

$stmt = $pdo->prepare("UPDATE defense_schedules SET status = 'Cancelled', updated_at = CURRENT_TIMESTAMP WHERE schedule_id = ? AND adviser_id = ?");
$stmt->execute([$data["schedule_id"], $data["professor_id"]]);
if ($stmt->rowCount() === 0) send_response(false, "Only the adviser can cancel this schedule.");

notify_schedule_users($pdo, $data["schedule_id"], "Defense schedule cancelled", "A defense schedule has been cancelled.");
send_response(true, "Schedule cancelled successfully.");
?>
