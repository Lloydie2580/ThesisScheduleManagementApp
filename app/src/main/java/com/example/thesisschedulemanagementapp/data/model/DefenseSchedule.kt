package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class DefenseSchedule(
    @SerializedName("schedule_id") val scheduleId: Int = 0,
    @SerializedName("group_id") val groupId: Int = 0,
    @SerializedName("group_code") val groupCode: String? = null,
    @SerializedName("research_title") val researchTitle: String? = null,
    @SerializedName("adviser_id") val adviserId: Int = 0,
    @SerializedName("adviser_name") val adviserName: String? = null,
    @SerializedName("room_id") val roomId: Int = 0,
    @SerializedName("room_name") val roomName: String? = null,
    @SerializedName("defense_date") val defenseDate: String? = null,
    @SerializedName("start_time") val startTime: String? = null,
    @SerializedName("end_time") val endTime: String? = null,
    val status: String? = null,
    @SerializedName("adviser_approved") val adviserApproved: Boolean = false,
    @SerializedName("panelists") val panelists: List<Professor>? = emptyList()
)
