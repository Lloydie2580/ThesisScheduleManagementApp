package com.example.thesisschedulemanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thesisschedulemanagementapp.data.model.AppMessage
import com.example.thesisschedulemanagementapp.data.model.Professor
import com.example.thesisschedulemanagementapp.data.model.Room
import com.example.thesisschedulemanagementapp.data.model.ScheduleRequest
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.data.repository.GroupRepository
import com.example.thesisschedulemanagementapp.data.repository.ProfessorRepository
import com.example.thesisschedulemanagementapp.data.repository.RoomRepository
import com.example.thesisschedulemanagementapp.data.repository.ScheduleRepository
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleManagementViewModel : ViewModel() {
    private val scheduleRepository = ScheduleRepository()
    private val groupRepository = GroupRepository()
    private val professorRepository = ProfessorRepository()
    private val roomRepository = RoomRepository()

    private val _message = MutableStateFlow<AppMessage?>(null)
    val message: StateFlow<AppMessage?> = _message.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _groups = MutableStateFlow<List<StudentGroup>>(emptyList())
    val groups: StateFlow<List<StudentGroup>> = _groups.asStateFlow()

    private val _professors = MutableStateFlow<List<Professor>>(emptyList())
    val professors: StateFlow<List<Professor>> = _professors.asStateFlow()

    val rooms: List<Room> = roomRepository.defaultRooms()
    val statuses = listOf("Pending", "Scheduled", "Rescheduled", "Completed", "Cancelled")

    fun loadOptions(adviserId: Int) {
        viewModelScope.launch {
            groupRepository.getGroups(adviserId).onSuccess { _groups.value = it }
            professorRepository.getProfessors().onSuccess { _professors.value = it }
        }
    }

    fun create(request: ScheduleRequest) = submit { scheduleRepository.create(request) }
    fun update(request: ScheduleRequest) = submit { scheduleRepository.update(request) }
    fun cancel(scheduleId: Int, professorId: Int) = submit { scheduleRepository.cancel(scheduleId, professorId) }
    fun complete(scheduleId: Int, professorId: Int) = submit { scheduleRepository.complete(scheduleId, professorId) }
    fun delete(scheduleId: Int, professorId: Int) = submit { scheduleRepository.delete(scheduleId, professorId) }

    fun validate(
        groupId: Int,
        researchTitle: String,
        date: String,
        startTime: String,
        endTime: String,
        roomId: Int,
        panelistIds: List<Int>
    ): String? {
        if (groupId == 0 || researchTitle.isBlank() || date.isBlank() || startTime.isBlank() || endTime.isBlank() || roomId == 0) {
            return "Complete all schedule fields."
        }
        if (!isValidDate(date)) return "Date must use MM/DD/YYYY format."
        val start = parseTime(startTime) ?: return "Start time must use H:MM AM or H:MM PM format."
        val end = parseTime(endTime) ?: return "End time must use H:MM AM or H:MM PM format."
        if (end <= start) return "End time must be after start time."
        if (panelistIds.isEmpty()) return "Select at least one panelist."
        if (panelistIds.size > 2) return "A maximum of 2 panelists can be selected."
        return null
    }

    fun setMessage(message: String, success: Boolean = false) {
        _message.value = AppMessage(success, message)
    }

    fun clearMessage() {
        _message.value = null
    }

    private fun submit(block: suspend () -> AppMessage) {
        viewModelScope.launch {
            _loading.value = true
            _message.value = block()
            _loading.value = false
        }
    }

    fun calculateEndTime(startTime: String): String? {
        val parsedStart = parseTime(startTime) ?: return null
        val calendar = Calendar.getInstance().apply {
            timeInMillis = parsedStart
            add(Calendar.HOUR_OF_DAY, 3)
        }
        return timeFormatter().format(calendar.time)
    }

    private fun isValidDate(value: String): Boolean {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        formatter.isLenient = false
        return runCatching { formatter.parse(value) != null }.getOrDefault(false)
    }

    private fun parseTime(value: String): Long? {
        val formatter = timeFormatter()
        formatter.isLenient = false
        return runCatching { formatter.parse(value.uppercase(Locale.US))?.time }.getOrNull()
    }

    private fun timeFormatter(): SimpleDateFormat = SimpleDateFormat("h:mm a", Locale.US).apply {
        isLenient = false
    }
}
