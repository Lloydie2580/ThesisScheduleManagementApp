package com.example.thesisschedulemanagementapp.viewmodel

data class UiState<T>(
    val loading: Boolean = false,
    val data: T? = null,
    val message: String? = null,
    val success: Boolean = false
)
