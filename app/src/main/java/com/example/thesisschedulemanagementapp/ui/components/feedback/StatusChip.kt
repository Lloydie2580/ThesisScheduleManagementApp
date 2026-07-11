package com.example.thesisschedulemanagementapp.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thesisschedulemanagementapp.ui.models.ScheduleStatus
import com.example.thesisschedulemanagementapp.ui.theme.ErrorColor
import com.example.thesisschedulemanagementapp.ui.theme.Primary
import com.example.thesisschedulemanagementapp.ui.theme.Secondary
import com.example.thesisschedulemanagementapp.ui.theme.Success
import com.example.thesisschedulemanagementapp.ui.theme.Warning

@Composable
fun StatusChip(
    status: ScheduleStatus
) {

    val color = when (status) {
        ScheduleStatus.PENDING -> Warning
        ScheduleStatus.SCHEDULED -> Primary
        ScheduleStatus.RESCHEDULED -> Secondary
        ScheduleStatus.COMPLETED -> Success
        ScheduleStatus.CANCELLED -> ErrorColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = status.displayName,
            color = color,
            style = MaterialTheme.typography.labelLarge.let {
                it.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            },
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}