<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["email", "password"]);

$stmt = $pdo->prepare("SELECT user_id, full_name, email, password_hash, role FROM users WHERE email = ?");
$stmt->execute([trim($data["email"])]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$user || !password_verify($data["password"], $user["password_hash"])) {
    send_response(false, "Invalid email or password.");
}

unset($user["password_hash"]);
$user["user_id"] = (int)$user["user_id"];
send_response(true, "Login successful.", $user);
?>
