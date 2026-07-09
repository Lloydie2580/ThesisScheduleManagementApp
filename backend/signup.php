<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["full_name", "email", "password", "role"]);

$role = ucfirst(strtolower(trim($data["role"])));
if ($role !== "Student" && $role !== "Professor") {
    send_response(false, "Role must be Student or Professor.");
}

$stmt = $pdo->prepare("SELECT user_id FROM users WHERE email = ?");
$stmt->execute([trim($data["email"])]);
if ($stmt->fetch()) {
    send_response(false, "Email is already registered.");
}

$password_hash = password_hash($data["password"], PASSWORD_DEFAULT);
$stmt = $pdo->prepare("INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)");
$stmt->execute([trim($data["full_name"]), trim($data["email"]), $password_hash, $role]);

send_response(true, "Account created successfully.");
?>
