package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.api.ApiClient
import com.example.thesisschedulemanagementapp.data.model.NotificationItem

class NotificationRepository {
    suspend fun getNotifications(userId: Int): Result<List<NotificationItem>> = runCatching {
        val json = ApiClient.get("get_notifications.php", mapOf("user_id" to userId))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.listFromData<NotificationItem>(json)
    }
}
