package com.example.thesisschedulemanagementapp.ui.screens.shared

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.NotificationList
import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotificationsScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    studentViewModel: StudentDashboardViewModel = viewModel(),
    professorViewModel: ProfessorDashboardViewModel = viewModel(),
    onBack: () -> Unit
) {
    val studentNotifications by studentViewModel.notifications.collectAsState()
    val professorNotifications by professorViewModel.notifications.collectAsState()

    val state = if (user?.role.equals("student", true)) {
        studentNotifications
    } else {
        professorNotifications
    }

    ScreenScaffold(snackbarHostState) {
        BackHeader(
            title = "Notifications",
            onBack = onBack
        )

        if (state.loading) {
            CircularProgressIndicator()
        }

        NotificationList(items = state.data.orEmpty())
    }
}

@Preview(
    showBackground = true,
    widthDp = 390
)
@Composable
private fun NotificationsScreenPreview() {
    ThesisScheduleManagementTheme {
        NotificationsScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            onBack = {}
        )
    }
}