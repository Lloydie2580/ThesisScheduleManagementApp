<?php

function read_json_input() {
    $raw = file_get_contents("php://input");
    $data = json_decode($raw, true);
    return is_array($data) ? $data : [];
}

function send_response($success, $message, $data = null) {
    if (ob_get_length()) ob_clean();
    $response = [
        "success" => $success,
        "message" => $message
    ];
    if ($data !== null) {
        $response["data"] = $data;
    }
    echo json_encode($response);
    exit;
}

function require_fields($data, $fields) {
    foreach ($fields as $field) {
        if (!isset($data[$field]) || $data[$field] === "") {
            send_response(false, "$field is required.");
        }
    }
}

function parse_date_input($value) {
    $date = DateTime::createFromFormat("m/d/Y", trim($value));
    if (!$date || $date->format("m/d/Y") !== trim($value)) {
        send_response(false, "Date must use MM/DD/YYYY format.");
    }
    return $date->format("Y-m-d");
}

function parse_time_input($value) {
    $value = strtoupper(trim($value));
    $time = DateTime::createFromFormat("g:i A", $value);
    if (!$time || $time->format("g:i A") !== ltrim($value, "0")) {
        $time = DateTime::createFromFormat("h:i A", $value);
    }
    if (!$time || ($time->format("g:i A") !== ltrim($value, "0") && $time->format("h:i A") !== $value)) {
        send_response(false, "Time must use H:MM AM or H:MM PM format.");
    }
    return $time->format("H:i:s");
}

function format_date_output($value) {
    return date("m/d/Y", strtotime($value));
}

function format_time_output($value) {
    return date("g:i A", strtotime($value));
}

function get_schedule_panelists($pdo, $schedule_id) {
    try {
        $stmt = $pdo->prepare("
            SELECT u.user_id, u.full_name, u.email, sp.is_approved
            FROM schedule_panelists sp
            JOIN users u ON sp.professor_id = u.user_id
            WHERE sp.schedule_id = ?
            ORDER BY u.full_name
        ");
        $stmt->execute([$schedule_id]);
        $panelists = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($panelists as &$p) {
            $p["is_approved"] = (bool)($p["is_approved"] ?? 0);
        }
        return $panelists;
    } catch (PDOException $e) {
        $stmt = $pdo->prepare("
            SELECT u.user_id, u.full_name, u.email
            FROM schedule_panelists sp
            JOIN users u ON sp.professor_id = u.user_id
            WHERE sp.schedule_id = ?
            ORDER BY u.full_name
        ");
        $stmt->execute([$schedule_id]);
        $panelists = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($panelists as &$p) {
            $p["is_approved"] = false;
        }
        return $panelists;
    }
}

function format_schedule($pdo, $row) {
    $row["schedule_id"] = (int)$row["schedule_id"];
    $row["group_id"] = (int)$row["group_id"];
    $row["adviser_id"] = (int)$row["adviser_id"];
    $row["room_id"] = (int)$row["room_id"];
    $row["adviser_approved"] = (bool)($row["adviser_approved"] ?? 0);
    $row["defense_date"] = format_date_output($row["defense_date"]);
    $row["start_time"] = format_time_output($row["start_time"]);
    $row["end_time"] = format_time_output($row["end_time"]);
    $row["panelists"] = get_schedule_panelists($pdo, $row["schedule_id"]);
    return $row;
}

function check_schedule_completion($pdo, $schedule_id) {
    $stmt = $pdo->prepare("SELECT adviser_approved FROM defense_schedules WHERE schedule_id = ?");
    $stmt->execute([$schedule_id]);
    $adviser_approved = (bool)$stmt->fetchColumn();

    if (!$adviser_approved) return false;

    $stmt = $pdo->prepare("SELECT COUNT(*) FROM schedule_panelists WHERE schedule_id = ? AND is_approved = 0");
    $stmt->execute([$schedule_id]);
    $pending_panelists = (int)$stmt->fetchColumn();

    if ($pending_panelists === 0) {
        $stmt = $pdo->prepare("UPDATE defense_schedules SET status = 'Scheduled' WHERE schedule_id = ?");
        $stmt->execute([$schedule_id]);
        return true;
    }
    return false;
}

function schedule_conflict_message($pdo, $group_id, $adviser_id, $room_id, $date, $start, $end, $panelist_ids, $exclude_schedule_id = null) {
    $exclude_sql = $exclude_schedule_id ? " AND schedule_id <> ?" : "";
    $exclude_panel_sql = $exclude_schedule_id ? " AND ds.schedule_id <> ?" : "";

    $params = [$room_id, $date, $start, $end];
    if ($exclude_schedule_id) $params[] = $exclude_schedule_id;
    $stmt = $pdo->prepare("
        SELECT schedule_id FROM defense_schedules
        WHERE room_id = ? AND defense_date = ? AND status <> 'Cancelled'
        AND start_time < ? AND end_time > ? $exclude_sql
        LIMIT 1
    ");
    $stmt->execute($params);
    if ($stmt->fetch()) return "Room is already booked for the selected date and time.";

    $params = [$adviser_id, $date, $start, $end];
    if ($exclude_schedule_id) $params[] = $exclude_schedule_id;
    $stmt = $pdo->prepare("
        SELECT schedule_id FROM defense_schedules
        WHERE adviser_id = ? AND defense_date = ? AND status <> 'Cancelled'
        AND start_time < ? AND end_time > ? $exclude_sql
        LIMIT 1
    ");
    $stmt->execute($params);
    if ($stmt->fetch()) return "Adviser already has another schedule for the selected date and time.";

    $params = [$group_id];
    if ($exclude_schedule_id) $params[] = $exclude_schedule_id;
    $stmt = $pdo->prepare("
        SELECT schedule_id FROM defense_schedules
        WHERE group_id = ? AND status <> 'Cancelled' $exclude_sql
        LIMIT 1
    ");
    $stmt->execute($params);
    if ($stmt->fetch()) return "Student group already has another defense schedule.";

    foreach ($panelist_ids as $panelist_id) {
        $params = [$panelist_id, $date, $start, $end];
        if ($exclude_schedule_id) $params[] = $exclude_schedule_id;
        $stmt = $pdo->prepare("
            SELECT ds.schedule_id
            FROM defense_schedules ds
            JOIN schedule_panelists sp ON ds.schedule_id = sp.schedule_id
            WHERE sp.professor_id = ? AND ds.defense_date = ? AND ds.status <> 'Cancelled'
            AND ds.start_time < ? AND ds.end_time > ? $exclude_panel_sql
            LIMIT 1
        ");
        $stmt->execute($params);
        if ($stmt->fetch()) return "One or more panelists already have another schedule for the selected date and time.";
    }

    return null;
}

function notify_user($pdo, $user_id, $title, $message) {
    $stmt = $pdo->prepare("INSERT INTO notifications (user_id, title, message) VALUES (?, ?, ?)");
    $stmt->execute([$user_id, $title, $message]);
}

function notify_schedule_users($pdo, $schedule_id, $title, $message) {
    $stmt = $pdo->prepare("SELECT group_id, adviser_id FROM defense_schedules WHERE schedule_id = ?");
    $stmt->execute([$schedule_id]);
    $schedule = $stmt->fetch(PDO::FETCH_ASSOC);
    if (!$schedule) return;

    notify_user($pdo, $schedule["adviser_id"], $title, $message);

    $stmt = $pdo->prepare("SELECT student_id FROM group_members WHERE group_id = ?");
    $stmt->execute([$schedule["group_id"]]);
    foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $member) {
        notify_user($pdo, $member["student_id"], $title, $message);
    }

    $stmt = $pdo->prepare("SELECT professor_id FROM schedule_panelists WHERE schedule_id = ?");
    $stmt->execute([$schedule_id]);
    foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $panelist) {
        notify_user($pdo, $panelist["professor_id"], $title, $message);
    }
}
