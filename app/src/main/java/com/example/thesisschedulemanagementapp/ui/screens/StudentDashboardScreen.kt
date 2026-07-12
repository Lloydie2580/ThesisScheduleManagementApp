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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
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
    val groupState by viewModel.studentGroup.collectAsState()
    
    LaunchedEffect(user?.userId) { user?.let { viewModel.load(it.userId) } }
    
    ScreenScaffold(snackbarHostState) {
        DashboardHeader("Student Dashboard", user, onLogout)
        ActionRow(
            "View Schedule" to onOpenSchedule,
            "Notifications" to onOpenNotifications
        )

        HorizontalScrollSection("My Schedule", onAction = onRequestSchedule, actionLabel = "Request Schedule") {
            if (scheduleState.loading) CircularProgressIndicator()
            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    scheduleState.data?.let { ScheduleCard(it, canManage = false, currentUserId = user?.userId) }
                        ?: Text("No defense schedule has been assigned yet.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Text("Recent Updates", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        NotificationList(notificationState.data.orEmpty().take(3))
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun StudentDashboardScreenPreview() {
    ThesisScheduleManagementTheme {
        StudentDashboardScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            viewModel = StudentDashboardViewModel(),
            onOpenSchedule = {},
            onRequestSchedule = {},
            onOpenNotifications = {},
            onLogout = {}
        )
    }
}
