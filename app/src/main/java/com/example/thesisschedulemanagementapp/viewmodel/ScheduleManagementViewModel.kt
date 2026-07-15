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
            groupRepository.getGroups(adviserId).fold(
                onSuccess = { _groups.value = it },
                onFailure = { setMessage("Couldn't load groups: ${it.message ?: "unknown error"}") }
            )
            professorRepository.getProfessors().fold(
                onSuccess = { _professors.value = it },
                onFailure = { setMessage("Couldn't load professors: ${it.message ?: "unknown error"}") }
            )
        }
    }

    fun loadStudentOptions(studentId: Int) {
        viewModelScope.launch {
            groupRepository.getStudentGroup(studentId).fold(
                onSuccess = { group -> _groups.value = group?.let { listOf(it) } ?: emptyList() },
                onFailure = { setMessage("Couldn't load your group: ${it.message ?: "unknown error"}") }
            )
            professorRepository.getProfessors().fold(
                onSuccess = { _professors.value = it },
                onFailure = { setMessage("Couldn't load professors: ${it.message ?: "unknown error"}") }
            )
        }
    }

    fun create(request: ScheduleRequest) = submit { scheduleRepository.create(request) }
    fun update(request: ScheduleRequest) = submit { scheduleRepository.update(request) }
    fun approve(scheduleId: Int, professorId: Int, professorName: String) = submit { scheduleRepository.approve(scheduleId, professorId, professorName) }
    fun cancelApproval(scheduleId: Int, professorId: Int, professorName: String) = submit { scheduleRepository.cancelApproval(scheduleId, professorId, professorName) }
    fun reject(scheduleId: Int, professorId: Int) = submit { scheduleRepository.reject(scheduleId, professorId) }
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
        if (groupId == 0) return "Please select a student group."
        if (researchTitle.isBlank()) return "Research title cannot be empty."
        if (date.isBlank()) return "Please select a defense date."
        
        // Date Validation: Must be at least 3 days from today
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val selectedDate = runCatching { sdf.parse(date) }.getOrNull() ?: return "Invalid date format."
        
        val minDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 3)
        }.time

        if (selectedDate.before(minDate)) {
            return "Schedules must be booked at least 3 days in advance."
        }

        if (startTime.isBlank()) return "Please select a start time."
        if (endTime.isBlank()) return "End time is missing (should be 3 hours after start)."
        
        val start = parseTime(startTime) ?: return "Start time format is invalid."
        val end = parseTime(endTime) ?: return "End time format is invalid."
        
        // Time Range Validation: 8:00 AM to 6:00 PM
        val startCal = Calendar.getInstance().apply { timeInMillis = start }
        val endCal = Calendar.getInstance().apply { timeInMillis = end }
        
        val startHour = startCal.get(Calendar.HOUR_OF_DAY)
        val endHour = endCal.get(Calendar.HOUR_OF_DAY)
        val endMinute = endCal.get(Calendar.MINUTE)

        if (startHour < 8 || endHour > 18 || (endHour == 18 && endMinute > 0)) {
            return "The time is invalid. Select only the right time."
        }

        if (end <= start) return "End time must be after start time."
        if (roomId == 0) return "Please select a room."
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

    private fun parseTime(value: String): Long? {
        val formatter = timeFormatter()
        formatter.isLenient = false
        return runCatching { formatter.parse(value.uppercase(Locale.US))?.time }.getOrNull()
            ?: runCatching { formatter.parse(value)?.time }.getOrNull()
    }

    private fun timeFormatter(): SimpleDateFormat = SimpleDateFormat("h:mm a", Locale.US).apply {
        isLenient = false
    }
}
