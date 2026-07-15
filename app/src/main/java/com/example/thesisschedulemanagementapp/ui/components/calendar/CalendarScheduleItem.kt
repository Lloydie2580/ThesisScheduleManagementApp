package com.example.thesisschedulemanagementapp.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.ui.components.models.ScheduleStatus
import com.example.thesisschedulemanagementapp.ui.theme.*
import androidx.compose.ui.Alignment

@Composable
fun CalendarScheduleItem(
    schedule: DefenseSchedule,
    modifier: Modifier = Modifier
) {

    val statusColor = scheduleColor(schedule)

    val statusText = when(schedule.status?.uppercase()) {
        "APPROVED" -> "Scheduled"
        "PENDING" -> "Pending"
        "COMPLETED" -> "Complete"
        "REJECTED" -> "Rejected"
        "CANCELLED" -> "Cancelled"
        else -> "Scheduled"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = statusColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = statusColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = schedule.groupCode ?: "Unknown Group",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            ) {

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )

            }
        }

        Text(
            text = schedule.researchTitle ?: "No Research Title",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )

        Text(
            text = "${schedule.startTime ?: "--"} - ${schedule.endTime ?: "--"}",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = schedule.roomName ?: "No Room",
            style = MaterialTheme.typography.bodySmall
        )
    }
}