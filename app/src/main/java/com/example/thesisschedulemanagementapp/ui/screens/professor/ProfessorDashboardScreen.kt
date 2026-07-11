package com.example.thesisschedulemanagementapp.ui.screens.professor

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.ActionRow
import com.example.thesisschedulemanagementapp.ui.screens.DashboardHeader
import com.example.thesisschedulemanagementapp.ui.screens.GroupCard
import com.example.thesisschedulemanagementapp.ui.screens.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.screens.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel

@Composable
fun ProfessorDashboardScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ProfessorDashboardViewModel,
    onOpenSchedules: () -> Unit,
    onCreateSchedule: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLogout: () -> Unit
) {
    val schedules by viewModel.schedules.collectAsState()
    val groups by viewModel.groups.collectAsState()
    LaunchedEffect(user?.userId) { user?.let { viewModel.load(it.userId) } }
    ScreenScaffold(snackbarHostState) {
        DashboardHeader("Professor Dashboard", user, onLogout)
        ActionRow(
            "Schedules" to onOpenSchedules,
            "Create" to onCreateSchedule,
            "Notifications" to onOpenNotifications
        )
        Text(
            "Advisee Groups",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        groups.data.orEmpty().take(4).forEach { GroupCard(it) }
        Text(
            "Assigned Schedules",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (schedules.loading) CircularProgressIndicator()
        schedules.data.orEmpty().take(3)
            .forEach { ScheduleCard(it, canManage = it.adviserId == user?.userId) }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ProfessorDashboardScreenPreview() {
    ThesisScheduleManagementTheme {
        ProfessorDashboardScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            viewModel = ProfessorDashboardViewModel(),
            onOpenSchedules = {},
            onCreateSchedule = {},
            onOpenNotifications = {},
            onLogout = {}
        )
    }
}
