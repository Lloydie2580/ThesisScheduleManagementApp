package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.api.ApiClient
import com.example.thesisschedulemanagementapp.data.model.Professor

class ProfessorRepository {
    suspend fun getProfessors(): Result<List<Professor>> = runCatching {
        val json = ApiClient.get("get_professors.php")
        if (!ApiClient.success(json)) error(ApiClient.message(json))
        ApiClient.listFromData<Professor>(json)
    }
}
