package com.example.thesisschedulemanagementapp.data.repository

import android.content.Context
import com.example.thesisschedulemanagementapp.data.model.User

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("thesis_session", Context.MODE_PRIVATE)

    fun save(user: User) {
        prefs.edit()
            .putInt("user_id", user.userId)
            .putString("full_name", user.fullName)
            .putString("email", user.email)
            .putString("role", user.role)
            .apply()
    }

    fun currentUser(): User? {
        val id = prefs.getInt("user_id", 0)
        if (id == 0) return null
        return User(
            userId = id,
            fullName = prefs.getString("full_name", "") ?: "",
            email = prefs.getString("email", "") ?: "",
            role = prefs.getString("role", "") ?: ""
        )
    }

    fun clear() = prefs.edit().clear().apply()
}
