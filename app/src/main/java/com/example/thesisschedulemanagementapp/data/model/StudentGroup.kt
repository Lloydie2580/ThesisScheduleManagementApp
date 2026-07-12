package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class StudentGroup(
    @SerializedName("group_id") val groupId: Int = 0,
    @SerializedName("group_code") val groupCode: String? = null,
    @SerializedName("research_title") val researchTitle: String? = null,
    @SerializedName("adviser_id") val adviserId: Int = 0,
    @SerializedName("adviser_name") val adviserName: String? = null,
    @SerializedName("members") val members: List<User>? = emptyList(),
    @SerializedName("panelists") val panelists: List<Professor>? = emptyList()
)
