package com.example.thesisschedulemanagementapp.ui.screens.student

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun StudentScheduleScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: StudentDashboardViewModel = viewModel(),
    onBack: () -> Unit
) {
    val scheduleState by viewModel.schedule.collectAsState()

    LaunchedEffect(user?.userId) {
        user?.let { viewModel.load(it.userId) }
    }

    ScreenScaffold(snackbarHostState) {

        BackHeader(title = "My Defense Schedule", onBack = onBack)

        SectionHeader(
            title = "Defense Details",
            subtitle = "Your assigned thesis defense schedule"
        )

        when {
            scheduleState.loading -> LoadingView(message = "Loading schedule...")
            scheduleState.data != null -> ScheduleCard(schedule = scheduleState.data!!)
            else -> EmptyState(
                title = "No Schedule Assigned",
                message = "Your adviser has not assigned a defense schedule yet."
            )
        }
    }
}