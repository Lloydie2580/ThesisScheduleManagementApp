package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.api.ApiClient
import com.example.thesisschedulemanagementapp.data.model.AppMessage
import com.example.thesisschedulemanagementapp.data.model.LoginRequest
import com.example.thesisschedulemanagementapp.data.model.SignupRequest
import com.example.thesisschedulemanagementapp.data.model.User

class AuthRepository(private val sessionManager: SessionManager) {
    suspend fun login(email: String, password: String): Result<User> = runCatching {
        val json = ApiClient.post("login.php", LoginRequest(email, password))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        val user = ApiClient.fromData<User>(json) ?: error("Login response is missing user data.")
        sessionManager.save(user)
        user
    }

    suspend fun signup(fullName: String, email: String, password: String, role: String): AppMessage = runCatching {
        val json = ApiClient.post("signup.php", SignupRequest(fullName, email, password, role))
        AppMessage(ApiClient.success(json), ApiClient.message(json))
    }.getOrElse { AppMessage(false, it.message ?: "Unable to sign up.") }

    suspend fun getStudents(): Result<List<User>> = runCatching {
        val json = ApiClient.get("get_students.php")
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.listFromData<User>(json)
    }
}
