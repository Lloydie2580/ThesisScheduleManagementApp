package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class NotificationItem(
    @SerializedName("notification_id") val notificationId: Int,
    val title: String,
    val message: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_read") val isRead: Int
)
