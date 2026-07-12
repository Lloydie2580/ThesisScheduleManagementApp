<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["schedule_id", "professor_id", "professor_name"]);

$schedule_id = $data["schedule_id"];
$professor_id = $data["professor_id"];
$professor_name = $data["professor_name"];

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

$pdo->beginTransaction();
try {
    if ($is_adviser) {
        $stmt = $pdo->prepare("UPDATE defense_schedules SET adviser_approved = 0 WHERE schedule_id = ?");
        $stmt->execute([$schedule_id]);
    } else if ($is_panelist) {
        $stmt = $pdo->prepare("UPDATE schedule_panelists SET is_approved = 0 WHERE schedule_id = ? AND professor_id = ?");
        $stmt->execute([$schedule_id, $professor_id]);
    }

    // Always set back to Pending if someone cancels
    $stmt = $pdo->prepare("UPDATE defense_schedules SET status = 'Pending' WHERE schedule_id = ?");
    $stmt->execute([$schedule_id]);

    notify_schedule_users($pdo, $schedule_id, "Approval Retracted", "$professor_name has retracted their approval. Schedule status reverted to Pending.");

    $pdo->commit();
    send_response(true, "Approval retracted successfully.");
} catch (Exception $e) {
    $pdo->rollBack();
    send_response(false, "Failed to retract approval: " . $e->getMessage());
}
