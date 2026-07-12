package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.api.ApiClient
import com.example.thesisschedulemanagementapp.data.model.AppMessage
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.ScheduleRequest

class ScheduleRepository {
    suspend fun getStudentSchedule(studentId: Int): Result<DefenseSchedule?> = runCatching {
        val json = ApiClient.get("get_student_schedule.php", mapOf("student_id" to studentId))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.fromData<DefenseSchedule>(json)
    }

    suspend fun getProfessorSchedules(professorId: Int): Result<List<DefenseSchedule>> = runCatching {
        val json = ApiClient.get("get_professor_schedules.php", mapOf("professor_id" to professorId))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.listFromData<DefenseSchedule>(json)
    }

    suspend fun create(request: ScheduleRequest): AppMessage = send("create_schedule.php", request)
    suspend fun update(request: ScheduleRequest): AppMessage = send("update_schedule.php", request)
    suspend fun cancel(scheduleId: Int, professorId: Int): AppMessage =
        send("cancel_schedule.php", mapOf("schedule_id" to scheduleId, "professor_id" to professorId))

    suspend fun complete(scheduleId: Int, professorId: Int): AppMessage =
        send("complete_schedule.php", mapOf("schedule_id" to scheduleId, "professor_id" to professorId))

    suspend fun approve(scheduleId: Int, professorId: Int, professorName: String): AppMessage =
        send("approve_schedule.php", mapOf("schedule_id" to scheduleId, "professor_id" to professorId, "professor_name" to professorName))

    suspend fun cancelApproval(scheduleId: Int, professorId: Int, professorName: String): AppMessage =
        send("cancel_approval.php", mapOf("schedule_id" to scheduleId, "professor_id" to professorId, "professor_name" to professorName))

    suspend fun reject(scheduleId: Int, professorId: Int): AppMessage =
        send("reject_schedule.php", mapOf("schedule_id" to scheduleId, "professor_id" to professorId))

    suspend fun delete(scheduleId: Int, professorId: Int): AppMessage =
        send("delete_schedule.php", mapOf("schedule_id" to scheduleId, "professor_id" to professorId))

    private suspend fun send(endpoint: String, body: Any): AppMessage = runCatching {
        val json = ApiClient.post(endpoint, body)
        AppMessage(ApiClient.success(json), ApiClient.message(json))
    }.getOrElse { AppMessage(false, it.message ?: "Request failed.") }
}
