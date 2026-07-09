package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class ScheduleRequest(
    @SerializedName("schedule_id") val scheduleId: Int? = null,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("research_title") val researchTitle: String,
    @SerializedName("defense_date") val defenseDate: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("adviser_id") val adviserId: Int,
    @SerializedName("panelist_ids") val panelistIds: List<Int>,
    val status: String
)

data class LoginRequest(val email: String, val password: String)

data class SignupRequest(
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val password: String,
    val role: String
)
