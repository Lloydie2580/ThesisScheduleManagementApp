package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class Professor(
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("full_name") val fullName: String? = null,
    val email: String? = null,
    @SerializedName("is_approved") val isApproved: Boolean = false
)
