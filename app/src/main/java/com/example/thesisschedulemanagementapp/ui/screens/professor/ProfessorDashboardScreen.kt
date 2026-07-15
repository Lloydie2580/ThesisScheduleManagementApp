package com.example.thesisschedulemanagementapp.ui.screens.professor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.GroupCard
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.DashboardHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfessorDashboardScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ProfessorDashboardViewModel = viewModel(),
    onOpenSchedules: () -> Unit,
    onCreateSchedule: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLogout: () -> Unit
) {
    val schedules by viewModel.schedules.collectAsState()
    val groups by viewModel.groups.collectAsState()

    LaunchedEffect(user?.userId) {
        user?.let { viewModel.load(it.userId) }
    }

    ScreenScaffold(snackbarHostState) {

        DashboardHeader(
            title = "Professor Dashboard",
            user = user,
            onLogout = onLogout,
            onNotifications = onOpenNotifications
        )

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

        SectionHeader(
            title = "Advisee Groups",
            subtitle = "Student groups assigned under your supervision"
        )

        if (groups.data.isNullOrEmpty()) {
            EmptyState(
                title = "No Advisee Groups",
                message = "No thesis groups are currently assigned to you."
            )
        } else {
            groups.data?.take(4)?.forEach { GroupCard(it) }
        }

        SectionHeader(
            title = "Assigned Schedules",
            subtitle = "Upcoming thesis defense schedules"
        )

        when {
            schedules.loading -> LoadingView(message = "Loading schedules...")
            schedules.data.isNullOrEmpty() -> EmptyState(
                title = "No Schedules",
                message = "Create a defense schedule to get started."
            )
            else -> {
                schedules.data?.take(3)?.forEach { schedule ->
                    ScheduleCard(
                        schedule = schedule,
                        canManage = schedule.adviserId == user?.userId,
                        onEdit = {},
                        onCancel = {},
                        onComplete = {},
                        onDelete = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ProfessorDashboardScreenPreview() {
    ThesisScheduleManagementTheme {
        ProfessorDashboardScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            onOpenSchedules = {},
            onCreateSchedule = {},
            onOpenNotifications = {},
            onLogout = {}
        )
    }
}