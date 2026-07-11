package com.example.thesisschedulemanagementapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.ui.theme.Dimens


@Composable
fun ScheduleCard(

    groupName: String,

    date: String,

    time: String,

    room: String,

    status: ScheduleStatus,

    modifier: Modifier = Modifier,

    onClick: () -> Unit = {}

) {


    AppCard(

        modifier = modifier.fillMaxWidth()

    ) {


        Column(

            verticalArrangement =
                Arrangement.spacedBy(Dimens.SpaceM)

        ) {


            Row {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(

                        text = groupName,

                        style = MaterialTheme.typography.titleLarge

                    )

                }


                StatusChip(status)

            }



            ScheduleInfoRow(
                Icons.Default.CalendarToday,
                date
            )


            ScheduleInfoRow(
                Icons.Default.Schedule,
                time
            )


            ScheduleInfoRow(
                Icons.Default.LocationOn,
                room
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
            Arrangement.spacedBy(Dimens.SpaceS)

    ){

        Icon(

            imageVector = icon,

            contentDescription = null

        )


        Text(

            text = text,

            style = MaterialTheme.typography.bodyMedium

        )

    }

}