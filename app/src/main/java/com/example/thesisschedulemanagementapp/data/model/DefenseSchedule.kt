package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class DefenseSchedule(
    @SerializedName("schedule_id") val scheduleId: Int,
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("group_code") val groupCode: String,
    @SerializedName("research_title") val researchTitle: String,
    @SerializedName("adviser_id") val adviserId: Int,
    @SerializedName("adviser_name") val adviserName: String,
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("room_name") val roomName: String,
    @SerializedName("defense_date") val defenseDate: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    val status: String,
    @SerializedName("panelists") val panelists: List<Professor> = emptyList()
)
