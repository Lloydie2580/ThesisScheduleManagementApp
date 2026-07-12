<?php
require_once "db.php";
require_once "helpers.php";

$professor_id = $_GET["professor_id"] ?? "";
if ($professor_id === "") send_response(false, "professor_id is required.");

$stmt = $pdo->prepare("
    SELECT DISTINCT ds.schedule_id, ds.group_id, sg.group_code, sg.research_title, ds.adviser_id, adviser.full_name AS adviser_name,
           ds.room_id, r.room_name, ds.defense_date, ds.start_time, ds.end_time, ds.status, ds.adviser_approved
    FROM defense_schedules ds
    JOIN student_groups sg ON ds.group_id = sg.group_id
    JOIN users adviser ON ds.adviser_id = adviser.user_id
    JOIN rooms r ON ds.room_id = r.room_id
    LEFT JOIN schedule_panelists sp ON ds.schedule_id = sp.schedule_id
    WHERE ds.adviser_id = ? OR sp.professor_id = ?
    ORDER BY ds.defense_date DESC, ds.start_time DESC
");
$stmt->execute([$professor_id, $professor_id]);

$schedules = [];
foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $row) {
    $schedules[] = format_schedule($pdo, $row);
}

send_response(true, "Schedules loaded.", $schedules);
?>
