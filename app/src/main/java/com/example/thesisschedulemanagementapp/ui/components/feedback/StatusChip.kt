package com.example.thesisschedulemanagementapp.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.ui.theme.ErrorColor
import com.example.thesisschedulemanagementapp.ui.theme.Success
import com.example.thesisschedulemanagementapp.ui.theme.Warning
import com.example.thesisschedulemanagementapp.ui.models.ScheduleStatus
import com.example.thesisschedulemanagementapp.ui.theme.Primary
import com.example.thesisschedulemanagementapp.ui.theme.Secondary

@Composable
fun StatusChip(
    status: ScheduleStatus
) {

    val color =
        when (status) {

            ScheduleStatus.PENDING ->
                Warning

            ScheduleStatus.SCHEDULED ->
                Primary

            ScheduleStatus.RESCHEDULED ->
                Secondary

            ScheduleStatus.COMPLETED ->
                Success

            ScheduleStatus.CANCELLED ->
                ErrorColor

        }

    Text(

        text = status.displayName,

        color = Color.White,

        style = MaterialTheme.typography.labelLarge,

        modifier = Modifier
            .background(
                color = color,
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )

    )

}