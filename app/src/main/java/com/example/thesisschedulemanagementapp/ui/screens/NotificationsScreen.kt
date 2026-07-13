package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.NotificationList
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun NotificationsScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    studentViewModel: StudentDashboardViewModel,
    professorViewModel: ProfessorDashboardViewModel,
    onBack: () -> Unit
) {
    val studentNotifications by studentViewModel.notifications.collectAsState()
    val professorNotifications by professorViewModel.notifications.collectAsState()

    val state =
        if (user?.role.equals("student", true))
            studentNotifications
        else
            professorNotifications

    ScreenScaffold(snackbarHostState) {

        BackHeader(
            title = "Notifications",
            onBack = onBack
        )

        when {
            state.loading -> {
                LoadingView(
                    message = "Loading notifications..."
                )
            }

            state.data.isNullOrEmpty() -> {
                EmptyState(
                    title = "No Notifications",
                    message = "You're all caught up."
                )
            }

            else -> {
                NotificationList(
                    items = state.data!!
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsPreview() {
    ThesisScheduleManagementTheme {
        NotificationsScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            studentViewModel = StudentDashboardViewModel(),
            professorViewModel = ProfessorDashboardViewModel(),
            onBack = {}
        )
    }
}