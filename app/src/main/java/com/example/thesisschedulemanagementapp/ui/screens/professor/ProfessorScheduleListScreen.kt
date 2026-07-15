package com.example.thesisschedulemanagementapp.ui.screens.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader
import com.example.thesisschedulemanagementapp.ui.components.feedback.ConfirmDialog
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel

@Composable
fun ProfessorScheduleListScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ProfessorDashboardViewModel,
    scheduleViewModel: ScheduleManagementViewModel,
    onBack: () -> Unit,
    onEdit: (DefenseSchedule) -> Unit
) {
    val schedules by viewModel.schedules.collectAsState()
    val message by scheduleViewModel.message.collectAsState()

    var scheduleToDelete by remember { mutableStateOf<DefenseSchedule?>(null) }

    LaunchedEffect(user?.userId) {
        user?.let { viewModel.load(it.userId) }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            scheduleViewModel.clearMessage()
            user?.let { currentUser -> viewModel.refreshSchedules(currentUser.userId) }
        }
    }

    ScreenScaffold(snackbarHostState) {

        BackHeader(title = "Professor Schedules", onBack = onBack)

        SectionHeader(
            title = "Defense Schedule Management",
            subtitle = "View and manage your assigned thesis defenses"
        )

        when {
            schedules.loading -> LoadingView(message = "Loading schedules...")
            schedules.data.isNullOrEmpty() -> EmptyState(
                title = "No Schedules Found",
                message = "Create your first thesis defense schedule."
            )
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
                    items(schedules.data.orEmpty()) { schedule ->
                        ScheduleCard(
                            schedule = schedule,
                            canManage = schedule.adviserId == user?.userId,
                            onEdit = { onEdit(schedule) },
                            onCancel = {
                                user?.let { scheduleViewModel.cancel(schedule.scheduleId, it.userId) }
                            },
                            onComplete = {
                                user?.let { scheduleViewModel.complete(schedule.scheduleId, it.userId) }
                            },
                            onDelete = { scheduleToDelete = schedule }
                        )
                    }
                }
            }
        }
    }

    scheduleToDelete?.let { schedule ->
        ConfirmDialog(
            title = "Delete Schedule",
            message = "This action permanently removes the defense schedule.",
            confirmText = "Delete",
            isDestructive = true,
            onConfirm = {
                user?.let { scheduleViewModel.delete(schedule.scheduleId, it.userId) }
                scheduleToDelete = null
            },
            onDismiss = { scheduleToDelete = null }
        )
    }
}