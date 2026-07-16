package com.example.thesisschedulemanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.NotificationItem
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.data.repository.GroupRepository
import com.example.thesisschedulemanagementapp.data.repository.NotificationRepository
import com.example.thesisschedulemanagementapp.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentDashboardViewModel : ViewModel() {
    private val scheduleRepository = ScheduleRepository()
    private val notificationRepository = NotificationRepository()

    private val _schedule = MutableStateFlow(UiState<DefenseSchedule?>())
    val schedule: StateFlow<UiState<DefenseSchedule?>> = _schedule.asStateFlow()

    private val _notifications = MutableStateFlow(UiState<List<NotificationItem>>())
    val notifications: StateFlow<UiState<List<NotificationItem>>> = _notifications.asStateFlow()

    private val _studentGroup = MutableStateFlow(UiState<StudentGroup?>())
    val studentGroup: StateFlow<UiState<StudentGroup?>> = _studentGroup.asStateFlow()

    private val groupRepository = GroupRepository()

    fun load(studentId: Int) {
        viewModelScope.launch {
            _schedule.value = UiState(loading = true)
            scheduleRepository.getStudentSchedule(studentId).fold(
                onSuccess = { _schedule.value = UiState(data = it, success = true) },
                onFailure = { _schedule.value = UiState(message = it.message, success = false) }
            )
            loadNotifications(studentId)
            loadStudentGroup(studentId)
        }
    }

    private fun loadStudentGroup(studentId: Int) {
        viewModelScope.launch {
            _studentGroup.value = UiState(loading = true)
            groupRepository.getStudentGroup(studentId).fold(
                onSuccess = { _studentGroup.value = UiState(data = it, success = true) },
                onFailure = { _studentGroup.value = UiState(data = null, message = it.message, success = false) }
            )
        }
    }

    fun loadNotifications(userId: Int) {
        viewModelScope.launch {
            _notifications.value = UiState(loading = true)
            notificationRepository.getNotifications(userId).fold(
                onSuccess = { _notifications.value = UiState(data = it, success = true) },
                onFailure = { _notifications.value = UiState(message = it.message, success = false) }
            )
        }
    }
}
