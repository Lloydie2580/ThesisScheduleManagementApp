package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.api.ApiClient
import com.example.thesisschedulemanagementapp.data.model.StudentGroup

class GroupRepository {
    suspend fun getGroups(adviserId: Int? = null): Result<List<StudentGroup>> = runCatching {
        val json = ApiClient.get("get_groups.php", mapOf("adviser_id" to adviserId))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.listFromData<StudentGroup>(json)
    }
}
