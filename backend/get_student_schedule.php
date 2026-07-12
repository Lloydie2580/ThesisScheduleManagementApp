<?php
require_once "db.php";
require_once "helpers.php";

$student_id = $_GET["student_id"] ?? "";
if ($student_id === "") send_response(false, "student_id is required.");

$stmt = $pdo->prepare("
    SELECT ds.schedule_id, ds.group_id, sg.group_code, sg.research_title, ds.adviser_id, adviser.full_name AS adviser_name,
           ds.room_id, r.room_name, ds.defense_date, ds.start_time, ds.end_time, ds.status, ds.adviser_approved
    FROM group_members gm
    JOIN student_groups sg ON gm.group_id = sg.group_id
    LEFT JOIN defense_schedules ds ON sg.group_id = ds.group_id
    LEFT JOIN users adviser ON ds.adviser_id = adviser.user_id
    LEFT JOIN rooms r ON ds.room_id = r.room_id
    WHERE gm.student_id = ?
    ORDER BY ds.defense_date DESC, ds.start_time DESC
    LIMIT 1
");
$stmt->execute([$student_id]);
$row = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$row || !$row["schedule_id"]) {
    send_response(true, "No schedule found.", null);
}

send_response(true, "Schedule loaded.", format_schedule($pdo, $row));
?>
