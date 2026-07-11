package com.example.thesisschedulemanagementapp.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.components.feedback.StatusChip
import com.example.thesisschedulemanagementapp.ui.components.models.ScheduleStatus
import com.example.thesisschedulemanagementapp.ui.theme.Dimens


@Composable
fun ScheduleCard(

    schedule: DefenseSchedule,

    modifier: Modifier = Modifier

) {

    AppCard(

        modifier = modifier.fillMaxWidth()

    ) {

        Column(

            verticalArrangement =
                Arrangement.spacedBy(
                    Dimens.SpaceM
                )

        ) {


            Row {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = schedule.researchTitle,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = schedule.groupCode,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }


                StatusChip(
                    status = ScheduleStatus.valueOf(
                        schedule.status
                    )
                )

            }



            ScheduleInfoRow(
                Icons.Default.CalendarToday,
                schedule.defenseDate
            )


            ScheduleInfoRow(
                Icons.Default.Schedule,
                "${schedule.startTime} - ${schedule.endTime}"
            )


            ScheduleInfoRow(
                Icons.Default.LocationOn,
                schedule.roomName
            )



            Text(
                text = "Adviser: ${schedule.adviserName}",
                style = MaterialTheme.typography.bodyMedium
            )


            Text(
                text =
                    "Panelists: ${
                        schedule.panelists.joinToString {
                            it.fullName
                        }
                    }",

                style = MaterialTheme.typography.bodyMedium

            )

        }

    }

}


@Composable
private fun ScheduleInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
){

    Row(
        horizontalArrangement =
            Arrangement.spacedBy(
                Dimens.SpaceS
            )
    ){

        Icon(
            imageVector = icon,
            contentDescription = null
        )


        Text(text)

    }

}