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

class ProfessorDashboardViewModel : ViewModel() {
    private val scheduleRepository = ScheduleRepository()
    private val groupRepository = GroupRepository()
    private val notificationRepository = NotificationRepository()

    private val _schedules = MutableStateFlow(UiState<List<DefenseSchedule>>())
    val schedules: StateFlow<UiState<List<DefenseSchedule>>> = _schedules.asStateFlow()

    private val _groups = MutableStateFlow(UiState<List<StudentGroup>>())
    val groups: StateFlow<UiState<List<StudentGroup>>> = _groups.asStateFlow()

    private val _notifications = MutableStateFlow(UiState<List<NotificationItem>>())
    val notifications: StateFlow<UiState<List<NotificationItem>>> = _notifications.asStateFlow()

    fun load(professorId: Int) {
        viewModelScope.launch {
            _schedules.value = UiState(loading = true)
            scheduleRepository.getProfessorSchedules(professorId).fold(
                onSuccess = { _schedules.value = UiState(data = it, success = true) },
                onFailure = { _schedules.value = UiState(message = it.message, success = false) }
            )
            groupRepository.getGroups(professorId).fold(
                onSuccess = { _groups.value = UiState(data = it, success = true) },
                onFailure = { _groups.value = UiState(message = it.message, success = false) }
            )
            notificationRepository.getNotifications(professorId).fold(
                onSuccess = { _notifications.value = UiState(data = it, success = true) },
                onFailure = { _notifications.value = UiState(message = it.message, success = false) }
            )
        }
    }

    fun refreshSchedules(professorId: Int) = load(professorId)
}
