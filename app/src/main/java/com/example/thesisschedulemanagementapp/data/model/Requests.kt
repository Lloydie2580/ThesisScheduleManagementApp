package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class ScheduleRequest(
    @SerializedName("schedule_id") val scheduleId: Int? = null,
    @SerializedName("group_id") val groupId: Int = 0,
    @SerializedName("research_title") val researchTitle: String? = null,
    @SerializedName("defense_date") val defenseDate: String? = null,
    @SerializedName("start_time") val startTime: String? = null,
    @SerializedName("end_time") val endTime: String? = null,
    @SerializedName("room_id") val roomId: Int = 0,
    @SerializedName("adviser_id") val adviserId: Int = 0,
    @SerializedName("panelist_ids") val panelistIds: List<Int>? = emptyList(),
    val status: String? = null,
    @SerializedName("requester_id") val requesterId: Int = 0,
    @SerializedName("requester_role") val requesterRole: String? = null
)

data class LoginRequest(val email: String, val password: String)

data class SignupRequest(
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val password: String,
    val role: String
)
