package com.example.thesisschedulemanagementapp.ui.screens.professor

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.shared.ScheduleFormScreen
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateScheduleScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ScheduleManagementViewModel = viewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(user?.userId) {
        user?.let { viewModel.loadOptions(it.userId) }
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