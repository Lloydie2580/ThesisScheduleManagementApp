package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.NotificationList
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.DashboardHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun StudentDashboardScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: StudentDashboardViewModel,
    onOpenSchedule: () -> Unit,
    onRequestSchedule: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLogout: () -> Unit
) {
    val scheduleState by viewModel.schedule.collectAsState()
    val notificationState by viewModel.notifications.collectAsState()

    LaunchedEffect(user?.userId) {
        user?.let { viewModel.load(it.userId) }
    }

    ScreenScaffold(snackbarHostState) {

        DashboardHeader(
            title = "Student Dashboard",
            user = user,
            onLogout = onLogout,
            onNotifications = onOpenNotifications
        )

        AppButton(
            text = "View My Schedule",
            icon = Icons.Default.CalendarMonth,
            buttonType = ButtonType.PRIMARY,
            onClick = onOpenSchedule
        )

        AppButton(
            text = "Request Schedule",
            icon = Icons.Default.Add,
            buttonType = ButtonType.OUTLINED,
            onClick = onRequestSchedule
        )

        SectionHeader(
            title = "Upcoming Defense"
        )

        when {
            scheduleState.loading -> {
                LoadingView(message = "Loading schedule...")
            }

            scheduleState.data != null -> {
                ScheduleCard(schedule = scheduleState.data!!)
            }

            else -> {
                EmptyState(
                    title = "No Schedule Yet",
                    message = "Your adviser has not assigned a thesis defense schedule yet."
                )
            }
        }

        SectionHeader(
            title = "Recent Updates"
        )

        NotificationList(
            items = notificationState.data.orEmpty().take(3)
        )
    }
}
