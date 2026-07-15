package com.example.thesisschedulemanagementapp.data.util

data class PasswordRequirement(
    val label: String,
    val satisfied: Boolean
)

object Validators {

    fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return pattern.matches(email.trim())
    }

    fun passwordRequirements(password: String): List<PasswordRequirement> = listOf(
        PasswordRequirement("At least 8 characters", password.length >= 8),
        PasswordRequirement("One uppercase letter", password.any { it.isUpperCase() }),
        PasswordRequirement("One lowercase letter", password.any { it.isLowerCase() }),
        PasswordRequirement("One number", password.any { it.isDigit() }),
        PasswordRequirement("One special character", password.any { !it.isLetterOrDigit() })
    )

    fun isPasswordValid(password: String): Boolean =
        passwordRequirements(password).all { it.satisfied }
}