package com.example.thesisschedulemanagementapp.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule

import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard

import com.example.thesisschedulemanagementapp.ui.components.feedback.StatusChip

import com.example.thesisschedulemanagementapp.ui.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.models.ScheduleStatus

import com.example.thesisschedulemanagementapp.ui.theme.Dimens


@Composable
fun ScheduleCard(

    schedule: DefenseSchedule,

    modifier: Modifier = Modifier,

    canManage: Boolean = false,

    onEdit: (() -> Unit)? = null,

    onCancel: (() -> Unit)? = null,

    onComplete: (() -> Unit)? = null,

    onDelete: (() -> Unit)? = null

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

                        style =
                            MaterialTheme.typography.titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        maxLines = 2,

                        overflow =
                            TextOverflow.Ellipsis

                    )



                    Text(

                        text =
                            "Group ${schedule.groupCode}",

                        style =
                            MaterialTheme.typography.bodyMedium,

                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant

                    )


                }



                StatusChip(

                    status =
                        ScheduleStatus.from(
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




            Column {


                Text(

                    text = "Adviser",

                    style =
                        MaterialTheme.typography.labelMedium,

                    color =
                        MaterialTheme.colorScheme.primary

                )


                Text(

                    text =
                        schedule.adviserName,

                    style =
                        MaterialTheme.typography.bodyMedium

                )


            }





            if(schedule.panelists.isNotEmpty()) {


                Column {


                    Text(

                        text = "Panelists",

                        style =
                            MaterialTheme.typography.labelMedium,

                        color =
                            MaterialTheme.colorScheme.primary

                    )


                    Text(

                        text =
                            schedule.panelists.joinToString {
                                it.fullName
                            },

                        style =
                            MaterialTheme.typography.bodyMedium

                    )


                }


            }





            if(canManage) {


                Divider()



                Text(

                    text = "Actions",

                    style =
                        MaterialTheme.typography.labelMedium,

                    color =
                        MaterialTheme.colorScheme.primary

                )



                FlowRow(

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            Dimens.SpaceS
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            Dimens.SpaceS
                        )

                ) {



                    onEdit?.let {


                        AppButton(

                            text = "Edit",

                            buttonType =
                                ButtonType.OUTLINED,

                            fullWidth = false,

                            onClick = it

                        )


                    }



                    onCancel?.let {


                        AppButton(

                            text = "Cancel",

                            buttonType =
                                ButtonType.OUTLINED,

                            fullWidth = false,

                            onClick = it

                        )


                    }



                    onComplete?.let {


                        AppButton(

                            text = "Complete",

                            buttonType =
                                ButtonType.PRIMARY,

                            fullWidth = false,

                            onClick = it

                        )


                    }



                    onDelete?.let {


                        AppButton(

                            text = "Delete",

                            buttonType =
                                ButtonType.DANGER,

                            fullWidth = false,

                            onClick = it

                        )


                    }


                }


            }


        }


    }


}



@Composable
private fun ScheduleInfoRow(

    icon: ImageVector,

    text: String

) {


    Row(

        horizontalArrangement =
            Arrangement.spacedBy(
                Dimens.SpaceS
            )

    ) {


        Icon(

            imageVector = icon,

            contentDescription = null,

            tint =
                MaterialTheme.colorScheme.primary

        )


        Text(

            text = text,

            style =
                MaterialTheme.typography.bodyMedium

        )


    }


}