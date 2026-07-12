package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.unit.dp
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
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
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

    LaunchedEffect(user?.userId) { user?.let { viewModel.load(it.userId) } }
    
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            scheduleViewModel.clearMessage()
            user?.let { u -> viewModel.refreshSchedules(u.userId) }
        }
    }

    ScreenScaffold(snackbarHostState) {
        DashboardHeader("Professor Dashboard", user, onLogout)
        ActionRow(
            "Schedules" to onOpenSchedules,
            "Notifications" to onOpenNotifications
        )
        
        HorizontalScrollSection("Advisee Groups", onAction = onCreateGroup, actionLabel = "Create Group") {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groups.data.orEmpty()) { GroupCard(it) }
            }
        }

        HorizontalScrollSection("Assigned Schedules", onAction = onCreateSchedule, actionLabel = "Create Schedule") {
            if (schedules.loading) CircularProgressIndicator()
            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(schedules.data.orEmpty()) { schedule ->
                    ScheduleCard(
                        schedule = schedule, 
                        canManage = schedule.adviserId == user?.userId,
                        currentUserId = user?.userId,
                        onEdit = { onEditSchedule(schedule) },
                        onApprove = { user?.let { scheduleViewModel.approve(schedule.scheduleId, it.userId, it.fullName ?: "Professor") } },
                        onRetractApproval = { user?.let { scheduleViewModel.cancelApproval(schedule.scheduleId, it.userId, it.fullName ?: "Professor") } },
                        onReject = { user?.let { scheduleViewModel.reject(schedule.scheduleId, it.userId) } }
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
