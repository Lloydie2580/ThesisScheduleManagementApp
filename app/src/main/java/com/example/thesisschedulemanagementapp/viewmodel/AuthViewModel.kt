package com.example.thesisschedulemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.data.repository.AuthRepository
import com.example.thesisschedulemanagementapp.data.repository.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    private val repository = AuthRepository(sessionManager)

    private val _state = MutableStateFlow(UiState<User>(data = sessionManager.currentUser()))
    val state: StateFlow<UiState<User>> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(message = "Email and password are required.", success = false) }
            return
        }
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.login(email.trim(), password).fold(
                onSuccess = { _state.value = UiState(data = it, message = "Login successful.", success = true) },
                onFailure = { _state.value = UiState(message = it.message ?: "Login failed.", success = false) }
            )
        }
    }

    fun signup(fullName: String, email: String, password: String, role: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || role.isBlank()) {
            _state.update { it.copy(message = "Complete all required fields and select a role.", success = false) }
            return
        }
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            val result = repository.signup(fullName.trim(), email.trim(), password, role)
            _state.value = UiState(message = result.message, success = result.success, data = sessionManager.currentUser())
        }
    }

    fun logout() {
        sessionManager.clear()
        _state.value = UiState()
    }

    fun clearMessage() = _state.update { it.copy(message = null) }
}
