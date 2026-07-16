package com.example.thesisschedulemanagementapp.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.components.feedback.StatusChip
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.components.models.ScheduleStatus
import com.example.thesisschedulemanagementapp.ui.theme.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.material3.IconButton

@Composable
fun ScheduleCard(
    schedule: DefenseSchedule,
    modifier: Modifier = Modifier,
    canManage: Boolean = false,
    currentUserId: Int? = null,
    onEdit: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onApprove: (() -> Unit)? = null,
    onRetractApproval: (() -> Unit)? = null,
    onReject: (() -> Unit)? = null
) {

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {

            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = schedule.researchTitle ?: "No Research Title",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Group ${schedule.groupCode ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.SpaceS))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)
                ) {

                    StatusChip(
                        status = ScheduleStatus.from(schedule.status ?: "")
                    )

                    if (canManage && onDelete != null) {
                        IconButton(
                            onClick = {
                                showDeleteDialog = true
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete Schedule",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {

                ScheduleInfoRow(
                    Icons.Default.CalendarToday,
                    schedule.defenseDate ?: "No Date",
                    ChipLavender,
                    ChipLavenderOn
                )

                ScheduleInfoRow(
                    Icons.Default.Schedule,
                    "${schedule.startTime ?: "--:--"} - ${schedule.endTime ?: "--:--"}",
                    ChipMint,
                    ChipMintOn
                )

                ScheduleInfoRow(
                    Icons.Default.LocationOn,
                    schedule.roomName ?: "No Room Assigned",
                    ChipPeach,
                    ChipPeachOn
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {
                Text(
                    text = "Adviser",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = schedule.adviserName ?: "Unassigned",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!schedule.panelists.isNullOrEmpty()) {

                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {

                    Text(
                        text = "Panelists",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXXS)) {
                        schedule.panelists!!.forEach { panelist ->
                            Text(
                                text = panelist.fullName ?: "Unknown",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Management Buttons
            if (canManage || onApprove != null || onRetractApproval != null || onReject != null) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
                ) {
                    // Common approval button (for both Adviser and Panelists if provided)
                    onApprove?.let {
                        AppButton(
                            text = "Approve",
                            buttonType = ButtonType.PRIMARY,
                            fullWidth = false,
                            onClick = it
                        )
                    }

                    if (canManage) {
                        onEdit?.let {
                            AppButton(
                                text = "Edit",
                                buttonType = ButtonType.OUTLINED,
                                fullWidth = false,
                                onClick = it
                            )
                        }

                        onCancel?.let {
                            AppButton(
                                text = "Cancel",
                                buttonType = ButtonType.OUTLINED,
                                fullWidth = false,
                                onClick = it
                            )
                        }
                    }

                    // Specific Panelist actions
                    onRetractApproval?.let {
                        AppButton(
                            text = "Retract",
                            buttonType = ButtonType.OUTLINED,
                            fullWidth = false,
                            onClick = it
                        )
                    }
                    onReject?.let {
                        AppButton(
                            text = "Reject",
                            buttonType = ButtonType.DANGER,
                            fullWidth = false,
                            onClick = it
                        )
                    }
                }
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete Schedule?")
            },
            text = {
                Text(
                    "Are you sure you want to delete this defense schedule? This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete?.invoke()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ScheduleInfoRow(
    icon: ImageVector,
    text: String,
    chipBg: Color,
    chipOn: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(chipBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = chipOn, modifier = Modifier.size(16.dp))
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
