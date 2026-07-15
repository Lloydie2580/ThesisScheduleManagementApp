<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["email", "password"]);

$stmt = $pdo->prepare("SELECT user_id, full_name, role, password_hash FROM users WHERE email = ?");
$stmt->execute([$data["email"]]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);

if ($user && password_verify($data["password"], $user["password_hash"])) {
    unset($user["password_hash"]);
    $user["user_id"] = (int)$user["user_id"];
    send_response(true, "Login successful.", $user);
} else {
    send_response(false, "Invalid email or password.");
}
