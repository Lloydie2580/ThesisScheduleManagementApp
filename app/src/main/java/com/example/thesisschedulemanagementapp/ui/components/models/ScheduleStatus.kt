package com.example.thesisschedulemanagementapp.ui.components.models

enum class ScheduleStatus(
    val displayName: String
) {
    PENDING("Pending"),
    SCHEDULED("Scheduled"),
    RESCHEDULED("Rescheduled"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    companion object {
        fun from(
            value: String?
        ): ScheduleStatus {
            return entries.firstOrNull {
                it.displayName.equals(
                    value,
                    ignoreCase = true
                )
            } ?: PENDING
        }
    }
}