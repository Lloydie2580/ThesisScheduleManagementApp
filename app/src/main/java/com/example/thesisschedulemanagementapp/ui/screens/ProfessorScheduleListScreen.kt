package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
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
    LaunchedEffect(user?.userId) { user?.let { viewModel.load(it.userId) } }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            scheduleViewModel.clearMessage()
            user?.let { u -> viewModel.refreshSchedules(u.userId) }
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            BackHeader("Professor Schedules", onBack)
            if (schedules.loading) CircularProgressIndicator()
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(schedules.data.orEmpty()) { schedule ->
                    ScheduleCard(
                        schedule = schedule,
                        canManage = schedule.adviserId == user?.userId,
                        onEdit = { onEdit(schedule) },
                        onCancel = { user?.let { scheduleViewModel.cancel(schedule.scheduleId, it.userId) } },
                        onComplete = { user?.let { scheduleViewModel.complete(schedule.scheduleId, it.userId) } },
                        onDelete = { scheduleToDelete = schedule }
                    )
                }
            }
        }
    }

    scheduleToDelete?.let { schedule ->
        AlertDialog(
            onDismissRequest = { scheduleToDelete = null },
            title = { Text("Delete Schedule") },
            text = { Text("This will permanently delete the selected defense schedule.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        user?.let { scheduleViewModel.delete(schedule.scheduleId, it.userId) }
                        scheduleToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { scheduleToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ProfessorScheduleListScreenPreview() {
    ThesisScheduleManagementTheme {
        ProfessorScheduleListScreen(
            snackbarHostState = remember { SnackbarHostState() },
            user = null,
            viewModel = ProfessorDashboardViewModel(),
            scheduleViewModel = ScheduleManagementViewModel(),
            onBack = {},
            onEdit = {}
        )
    }
}
