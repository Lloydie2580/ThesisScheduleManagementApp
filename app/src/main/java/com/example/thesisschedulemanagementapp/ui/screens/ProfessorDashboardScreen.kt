package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
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
    val schedulesState by viewModel.schedules.collectAsState()
    val groupsState by viewModel.groups.collectAsState()
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
            groupsState.loading -> LoadingView("Loading groups...")
            groupsState.data.isNullOrEmpty() -> EmptyState(
                title = "No Advisee Groups",
                message = "No thesis groups are currently assigned to you."
            )
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    groupsState.data!!.forEach { group ->
                        GroupCard(group)
                    }
                }
            }
        }

        if (!schedulesState.loading && !schedulesState.data.isNullOrEmpty()) {
            val allSchedules = schedulesState.data!!
            val adviseeSchedules = allSchedules.filter { it.adviserId == user?.userId }
            val panelistSchedules = allSchedules.filter { it.adviserId != user?.userId }

            if (adviseeSchedules.isNotEmpty()) {
                SectionHeader(
                    title = "My Advisee Defenses",
                    subtitle = "Schedules you created for your supervisees"
                )
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
                    adviseeSchedules.forEach { schedule ->
                        val hasApproved = schedule.adviserApproved
                        
                        ScheduleCard(
                            schedule = schedule,
                            canManage = true,
                            currentUserId = user?.userId,
                            onEdit = { onEditSchedule(schedule) },
                            onCancel = { user?.let { scheduleViewModel.cancel(schedule.scheduleId, it.userId) } },
                            onComplete = { user?.let { scheduleViewModel.complete(schedule.scheduleId, it.userId) } },
                            onDelete = { user?.let { scheduleViewModel.delete(schedule.scheduleId, it.userId) } },
                            // Show Approve button if the adviser hasn't approved yet (e.g. for student requests)
                            onApprove = if (!hasApproved) {
                                { user?.let { scheduleViewModel.approve(schedule.scheduleId, it.userId, it.fullName ?: "Professor") } }
                            } else null
                        )
                    }
                }
            }

            if (panelistSchedules.isNotEmpty()) {
                SectionHeader(
                    title = "Panelist Assignments",
                    subtitle = "Defenses where you are an invited evaluator"
                )
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
                    panelistSchedules.forEach { schedule ->
                        val myPanelistInfo = schedule.panelists?.find { it.userId == user?.userId }
                        val hasApproved = myPanelistInfo?.isApproved == true

                        ScheduleCard(
                            schedule = schedule,
                            canManage = false,
                            currentUserId = user?.userId,
                            onApprove = if (!hasApproved) {
                                { user?.let { scheduleViewModel.approve(schedule.scheduleId, it.userId, it.fullName ?: "Professor") } }
                            } else null,
                            onRetractApproval = if (hasApproved) {
                                { user?.let { scheduleViewModel.cancelApproval(schedule.scheduleId, it.userId, it.fullName ?: "Professor") } }
                            } else null,
                            onReject = { user?.let { scheduleViewModel.reject(schedule.scheduleId, it.userId) } }
                        )
                    }
                }
            }
        } else if (schedulesState.loading) {
            LoadingView("Loading schedules...")
        } else {
            SectionHeader(title = "Assigned Schedules")
            EmptyState(title = "No Schedules", message = "No upcoming defenses found.")
        }
    }
}
