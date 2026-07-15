package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.api.ApiClient
import com.example.thesisschedulemanagementapp.data.model.StudentGroup

class GroupRepository {
    suspend fun getGroups(adviserId: Int? = null): Result<List<StudentGroup>> = runCatching {
        val json = ApiClient.get("get_groups.php", mapOf("adviser_id" to adviserId))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.listFromData<StudentGroup>(json)
    }

    suspend fun createGroup(
        researchTitle: String,
        adviserId: Int,
        memberIds: List<Int>,
        panelistIds: List<Int>,
        program: String
    ): Result<Unit> = runCatching {
        val json = ApiClient.post("create_group.php", mapOf(
            "research_title" to researchTitle,
            "adviser_id" to adviserId,
            "member_ids" to memberIds,
            "panelist_ids" to panelistIds,
            "program" to program
        ))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
    }

    suspend fun getStudentGroup(studentId: Int): Result<StudentGroup?> = runCatching {
        val json = ApiClient.get("get_student_group.php", mapOf("student_id" to studentId))
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.fromData<StudentGroup>(json)
    }
}
