package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class Professor(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    val email: String
)
