package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.GroupCard
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.DashboardHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel

@Composable
fun ProfessorDashboardScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ProfessorDashboardViewModel,
    scheduleViewModel: ScheduleManagementViewModel,
    onOpenSchedules: () -> Unit,
    onEditSchedule: (DefenseSchedule) -> Unit,
    onCreateSchedule: () -> Unit,
    onCreateGroup: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLogout: () -> Unit
) {
    val schedules by viewModel.schedules.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val message by scheduleViewModel.message.collectAsState()

    LaunchedEffect(user?.userId) {
        user?.let { viewModel.load(it.userId) }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            scheduleViewModel.clearMessage()
            user?.let { u -> viewModel.refreshSchedules(u.userId) }
        }
    }

    ScreenScaffold(snackbarHostState) {
        DashboardHeader(
            title = "Professor Dashboard",
            user = user,
            onLogout = onLogout,
            onNotifications = onOpenNotifications
        )

        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
            AppButton(
                text = "Create Defense Schedule",
                icon = Icons.Default.AddCircle,
                buttonType = ButtonType.PRIMARY,
                onClick = onCreateSchedule
            )

            AppButton(
                text = "Manage Schedules",
                icon = Icons.Default.CalendarMonth,
                buttonType = ButtonType.OUTLINED,
                onClick = onOpenSchedules
            )

            AppButton(
                text = "Create Group",
                buttonType = ButtonType.OUTLINED,
                onClick = onCreateGroup
            )
        }

        SectionHeader(
            title = "Advisee Groups",
            subtitle = "Student groups assigned under your supervision"
        )

        when {
            groups.loading -> LoadingView("Loading groups...")
            groups.data.isNullOrEmpty() -> EmptyState(
                title = "No Advisee Groups",
                message = "No thesis groups are currently assigned to you."
            )
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    groups.data!!.forEach { group ->
                        GroupCard(group)
                    }
                }
            }
        }

        SectionHeader(
            title = "Assigned Schedules",
            subtitle = "Upcoming thesis defense schedules"
        )

        when {
            schedules.loading -> LoadingView("Loading schedules...")
            schedules.data.isNullOrEmpty() -> EmptyState(
                title = "No Schedules",
                message = "Create a defense schedule to get started."
            )
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
                    schedules.data!!.forEach { schedule ->
                        val isAdviser = schedule.adviserId == user?.userId
                        
                        ScheduleCard(
                            schedule = schedule,
                            canManage = isAdviser,
                            currentUserId = user?.userId,
                            onEdit = { onEditSchedule(schedule) },
                            onCancel = {
                                user?.let { scheduleViewModel.cancel(schedule.scheduleId, it.userId) }
                            },
                            onComplete = {
                                user?.let { scheduleViewModel.complete(schedule.scheduleId, it.userId) }
                            },
                            onDelete = {
                                user?.let { scheduleViewModel.delete(schedule.scheduleId, it.userId) }
                            },
                            onApprove = {
                                user?.let { scheduleViewModel.approve(schedule.scheduleId, it.userId, it.fullName ?: "Professor") }
                            },
                            onRetractApproval = {
                                user?.let { scheduleViewModel.cancelApproval(schedule.scheduleId, it.userId, it.fullName ?: "Professor") }
                            },
                            onReject = {
                                user?.let { scheduleViewModel.reject(schedule.scheduleId, it.userId) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfessorDashboardPreview() {
    ThesisScheduleManagementTheme {
        ProfessorDashboardScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            viewModel = ProfessorDashboardViewModel(),
            scheduleViewModel = ScheduleManagementViewModel(),
            onOpenSchedules = {},
            onEditSchedule = {},
            onCreateSchedule = {},
            onCreateGroup = {},
            onOpenNotifications = {},
            onLogout = {}
        )
    }
}
