package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.ScheduleFormScreen
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun CreateScheduleScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ScheduleManagementViewModel,
    studentDashboardViewModel: StudentDashboardViewModel? = null,
    onBack: () -> Unit
) {
    val isStudent = remember(user) { user?.role?.trim()?.equals("Student", true) == true }

    LaunchedEffect(user?.userId) {
        user?.let {
            if (isStudent) {
                viewModel.loadStudentOptions(it.userId)
            } else {
                viewModel.loadOptions(it.userId)
            }
        }
    }

    ScheduleFormScreen(
        title = if (isStudent) "Request Defense Schedule" else "Create Defense Schedule",
        snackbarHostState = snackbarHostState,
        user = user,
        viewModel = viewModel,
        schedule = null,
        onSubmit = viewModel::create,
        onBack = onBack
    )
}