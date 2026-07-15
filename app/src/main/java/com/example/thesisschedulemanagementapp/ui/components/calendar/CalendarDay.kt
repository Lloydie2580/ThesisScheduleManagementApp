package com.example.thesisschedulemanagementapp.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import java.time.LocalDate

@Composable
fun CalendarDay(
    date: LocalDate?,
    selected: Boolean,
    schedules: List<DefenseSchedule>,
    onClick: (LocalDate) -> Unit
) {
    if (date == null) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
        )
        return
    }

    Box(

        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primary.copy(.15f)
                else
                    Color.Transparent
            )
            .border(
                1.dp,
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick(date)
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                schedules.take(3).forEach {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(scheduleColor(it))
                    )
                }
            }
        }
    }
}

fun scheduleColor(schedule: DefenseSchedule): Color {
    return when (schedule.status?.uppercase()) {
        "APPROVED" -> Color(0xFF4CAF50)
        "PENDING" -> Color(0xFFFFC107)
        "REJECTED" -> Color.Red
        "COMPLETED" -> Color(0xFF7E57C2)
        "CANCELLED" -> Color.Gray
        else -> Color.Blue
    }
}