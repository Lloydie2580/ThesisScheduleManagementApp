package com.example.thesisschedulemanagementapp.ui.components.calendar

import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun parseScheduleDate(date: String?): LocalDate? {
    if (date.isNullOrBlank()) {
        return null
    }
    return try {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        LocalDate.parse(
            date,
            formatter
        )
    } catch (e: Exception) {
        null
    }
}

fun groupSchedulesByDate(
    schedules: List<DefenseSchedule>
): Map<LocalDate, List<DefenseSchedule>> {

    return schedules
        .mapNotNull { schedule ->
            parseScheduleDate(schedule.defenseDate)?.let {
                it to schedule
            }
        }
        .groupBy(
            { it.first },
            { it.second }
        )
}

fun daysInMonth(month: YearMonth): List<LocalDate?> {

    val firstDay = month.atDay(1)

    val leadingSpaces =
        (firstDay.dayOfWeek.value % 7)

    val days = mutableListOf<LocalDate?>()

    repeat(leadingSpaces) {
        days.add(null)
    }

    for (day in 1..month.lengthOfMonth()) {
        days.add(month.atDay(day))
    }

    while (days.size % 7 != 0) {
        days.add(null)
    }

    return days
}