package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
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
    LaunchedEffect(user?.userId) { 
        user?.let { 
            if (it.role.equals("student", true)) {
                viewModel.loadStudentOptions(it.userId)
            } else {
                viewModel.loadOptions(it.userId) 
            }
        } 
    }
    ScheduleFormScreen(
        title = "Create Defense Schedule",
        snackbarHostState = snackbarHostState,
        user = user,
        viewModel = viewModel,
        schedule = null,
        onSubmit = viewModel::create,
        onBack = onBack
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun CreateScheduleScreenPreview() {
    ThesisScheduleManagementTheme {
        CreateScheduleScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            viewModel = ScheduleManagementViewModel(),
            onBack = {}
        )
    }
}
