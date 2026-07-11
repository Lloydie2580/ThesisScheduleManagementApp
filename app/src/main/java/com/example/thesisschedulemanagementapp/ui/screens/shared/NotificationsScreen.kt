package com.example.thesisschedulemanagementapp.ui.screens.shared

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.BackHeader
import com.example.thesisschedulemanagementapp.ui.screens.NotificationList
import com.example.thesisschedulemanagementapp.ui.screens.ScreenScaffold
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
    val items = if (user?.role.equals("student", true)) {
        studentNotifications.data.orEmpty()
    } else {
        professorNotifications.data.orEmpty()
    }
    ScreenScaffold(snackbarHostState) {
        BackHeader("Notifications", onBack)
        NotificationList(items)
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun NotificationsScreenPreview() {
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
