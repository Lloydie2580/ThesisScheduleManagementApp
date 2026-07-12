<?php
require_once "db.php";
require_once "helpers.php";

$data = read_json_input();
require_fields($data, ["full_name", "email", "password", "role"]);

$password_hash = password_hash($data["password"], PASSWORD_DEFAULT);

try {
    $stmt = $pdo->prepare("INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)");
    $stmt->execute([$data["full_name"], $data["email"], $password_hash, $data["role"]]);
    send_response(true, "Account created successfully.");
} catch (PDOException $e) {
    if ($e->getCode() == 23000) {
        send_response(false, "Email address is already registered.");
    }
    send_response(false, "Unable to create account.");
}
