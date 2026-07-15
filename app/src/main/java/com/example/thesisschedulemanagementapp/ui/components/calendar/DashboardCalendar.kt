package com.example.thesisschedulemanagementapp.ui.components.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DashboardCalendar(
    schedules: List<DefenseSchedule>,
    user: User?,
    onEditSchedule: (DefenseSchedule) -> Unit
) {
    var currentMonth by remember {
        mutableStateOf(YearMonth.now())
    }
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val groupedSchedules = remember(schedules) {
        groupSchedulesByDate(schedules)
    }

    LaunchedEffect(schedules) { schedules.forEach { println("CALENDAR DATE: ${it.defenseDate}") } }

    val monthDays = remember(currentMonth) {
        daysInMonth(currentMonth)
    }

    val calendarRows = (monthDays.size + 6) / 7

    val calendarHeight = if (calendarRows == 6) {
        250.dp
    } else {
        220.dp
    }

    val selectedSchedules =
        groupedSchedules[selectedDate].orEmpty()

    AppCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalendarHeader(
                currentMonth = currentMonth,
                onPrevious = {
                    currentMonth = currentMonth.minusMonths(1)
                },
                onNext = {
                    currentMonth = currentMonth.plusMonths(1)
                }
            )
            WeekHeader()

            Column(
                modifier = Modifier.height(calendarHeight)
            ) {
                monthDays.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        week.forEach { day ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                CalendarDay(
                                    date = day,
                                    selected = day == selectedDate,
                                    schedules = groupedSchedules[day].orEmpty(),
                                    onClick = {
                                        selectedDate = it
                                    }
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
            Text(
                text = selectedDate.format(
                    DateTimeFormatter.ofPattern("MMMM d, yyyy")
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (selectedSchedules.isEmpty()) {
                Text(
                    "No schedules.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    selectedSchedules.forEach { schedule ->

                        CalendarScheduleItem(
                            schedule = schedule
                        )

                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    currentMonth: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) {
            Icon(
                Icons.Default.ChevronLeft,
                null
            )
        }
        Text(
            currentMonth.format(
                DateTimeFormatter.ofPattern("MMMM yyyy")
            ),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onNext) {
            Icon(
                Icons.Default.ChevronRight,
                null
            )
        }
    }
}

@Composable
private fun WeekHeader() {
    val days = listOf(
        "Sun",
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat"
    )
    Row(
        Modifier.fillMaxWidth()
    ) {
        days.forEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

