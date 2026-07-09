package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun StudentScheduleScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: StudentDashboardViewModel,
    onBack: () -> Unit
) {
    val scheduleState by viewModel.schedule.collectAsState()
    LaunchedEffect(user?.userId) { user?.let { viewModel.load(it.userId) } }
    ScreenScaffold(snackbarHostState) {
        BackHeader("My Defense Schedule", onBack)
        if (scheduleState.loading) CircularProgressIndicator()
        scheduleState.data?.let { ScheduleCard(it, canManage = false) }
            ?: Text("No defense schedule has been assigned yet.")
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun StudentScheduleScreenPreview() {
    ThesisScheduleManagementTheme {
        StudentScheduleScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            viewModel = StudentDashboardViewModel(),
            onBack = {}
        )
    }
}
