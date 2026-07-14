package com.example.thesisschedulemanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.NotificationItem
import com.example.thesisschedulemanagementapp.data.model.Professor
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.data.repository.AuthRepository
import com.example.thesisschedulemanagementapp.data.repository.GroupRepository
import com.example.thesisschedulemanagementapp.data.repository.NotificationRepository
import com.example.thesisschedulemanagementapp.data.repository.ProfessorRepository
import com.example.thesisschedulemanagementapp.data.repository.ScheduleRepository
import com.example.thesisschedulemanagementapp.data.repository.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfessorDashboardViewModel : ViewModel() {
    private val scheduleRepository = ScheduleRepository()
    private val groupRepository = GroupRepository()
    private val notificationRepository = NotificationRepository()
    private val professorRepository = ProfessorRepository()
    private lateinit var authRepository: AuthRepository

    private val _schedules = MutableStateFlow(UiState<List<DefenseSchedule>>())
    val schedules: StateFlow<UiState<List<DefenseSchedule>>> = _schedules.asStateFlow()

    private val _groups = MutableStateFlow(UiState<List<StudentGroup>>())
    val groups: StateFlow<UiState<List<StudentGroup>>> = _groups.asStateFlow()

    private val _notifications = MutableStateFlow(UiState<List<NotificationItem>>())
    val notifications: StateFlow<UiState<List<NotificationItem>>> = _notifications.asStateFlow()

    private val _allStudents = MutableStateFlow<List<User>>(emptyList())
    val allStudents: StateFlow<List<User>> = _allStudents.asStateFlow()

    private val _allProfessors = MutableStateFlow<List<Professor>>(emptyList())
    val allProfessors: StateFlow<List<Professor>> = _allProfessors.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun initRepositories(context: android.content.Context) {
        authRepository = AuthRepository(SessionManager(context))
    }

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

    fun loadCreationOptions() {
        viewModelScope.launch {
            professorRepository.getProfessors().onSuccess {
                _allProfessors.value = it
            }

            if (::authRepository.isInitialized) {
                authRepository.getStudents().onSuccess {
                    _allStudents.value = it
                }
            } else {
                _message.value = "AuthRepository NOT initialized"
            }
        }
    }

    fun createGroup(
        researchTitle: String,
        adviserId: Int,
        memberIds: List<Int>,
        panelistIds: List<Int>,
        program: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            groupRepository.createGroup(researchTitle, adviserId, memberIds, panelistIds, program).fold(
                onSuccess = { onSuccess() },
                onFailure = { onError(it.message ?: "Failed to create group") }
            )
        }
    }

    fun refreshSchedules(professorId: Int) = load(professorId)

    fun setMessage(msg: String?) {
        _message.value = msg
    }
}
