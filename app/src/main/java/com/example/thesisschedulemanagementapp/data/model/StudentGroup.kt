package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class StudentGroup(
    @SerializedName("group_id") val groupId: Int,
    @SerializedName("group_code") val groupCode: String,
    @SerializedName("research_title") val researchTitle: String,
    @SerializedName("adviser_id") val adviserId: Int,
    @SerializedName("adviser_name") val adviserName: String? = null,
    @SerializedName("members") val members: List<User> = emptyList()
)
