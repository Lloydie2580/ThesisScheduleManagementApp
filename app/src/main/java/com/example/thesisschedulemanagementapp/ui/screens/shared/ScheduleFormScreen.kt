package com.example.thesisschedulemanagementapp.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.ScheduleRequest
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.BackHeader
import com.example.thesisschedulemanagementapp.ui.screens.PickerField
import com.example.thesisschedulemanagementapp.ui.screens.PrimaryLoadingButton
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel

@Composable
internal fun ScheduleFormScreen(
    title: String,
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ScheduleManagementViewModel,
    schedule: DefenseSchedule?,
    onSubmit: (ScheduleRequest) -> Unit,
    onBack: () -> Unit
) {
    val groups by viewModel.groups.collectAsState()
    val professors by viewModel.professors.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    var groupId by remember(schedule) { mutableStateOf(schedule?.groupId ?: 0) }
    var researchTitle by remember(schedule) { mutableStateOf(schedule?.researchTitle ?: "") }
    var date by remember(schedule) { mutableStateOf(schedule?.defenseDate ?: "") }
    var startTime by remember(schedule) { mutableStateOf(schedule?.startTime ?: "") }
    var endTime by remember(schedule) { mutableStateOf(schedule?.endTime ?: "") }
    var roomId by remember(schedule) { mutableStateOf(schedule?.roomId ?: 0) }
    var status by remember(schedule) { mutableStateOf(schedule?.status ?: "Scheduled") }
    val selectedPanelists = remember(schedule) {
        mutableStateListOf<Int>().apply { addAll(schedule?.panelists?.map { it.userId }.orEmpty()) }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModel.clearMessage()
            if (it.success) onBack()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 960.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BackHeader(title, onBack)
                PickerField(
                    "Group Code",
                    groupId,
                    groups,
                    { it.groupId },
                    { it.groupCode }) { selectedGroupId ->
                    groupId = selectedGroupId
                    groups.firstOrNull { it.groupId == selectedGroupId }
                        ?.let { researchTitle = it.researchTitle }
                }
                OutlinedTextField(researchTitle, { researchTitle = it }, label = { Text("Research Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(date, { date = it }, label = { Text("Date (MM/DD/YYYY)") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        startTime,
                        {
                            startTime = it
                            viewModel.calculateEndTime(it)?.let { calculatedEndTime -> endTime = calculatedEndTime }
                        },
                        label = { Text("Start (H:MM AM/PM)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        endTime,
                        { endTime = it },
                        label = { Text("End (H:MM AM/PM)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                PickerField(
                    "Room",
                    roomId,
                    viewModel.rooms,
                    { it.roomId },
                    { it.roomName }) { roomId = it }
                PickerField("Status", status, viewModel.statuses, { it }, { it }) { status = it }
                Text("Panelists", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                professors.filter { it.userId != user?.userId }.forEach { professor ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPanelists.contains(professor.userId),
                            onCheckedChange = {
                                if (it) {
                                    if (selectedPanelists.size < 2) {
                                        selectedPanelists.add(professor.userId)
                                    } else {
                                        viewModel.setMessage("A maximum of 2 panelists can be selected.")
                                    }
                                } else {
                                    selectedPanelists.remove(professor.userId)
                                }
                            }
                        )
                        Text(professor.fullName)
                    }
                }
                PrimaryLoadingButton(
                    if (schedule == null) "Create Schedule" else "Save Changes",
                    loading
                ) {
                    val adviserId = user?.userId ?: 0
                    val validation = viewModel.validate(
                        groupId,
                        researchTitle,
                        date,
                        startTime,
                        endTime,
                        roomId,
                        selectedPanelists
                    )
                    if (validation != null) {
                        viewModel.setMessage(validation)
                    } else {
                        onSubmit(
                            ScheduleRequest(
                                scheduleId = schedule?.scheduleId,
                                groupId = groupId,
                                researchTitle = researchTitle,
                                defenseDate = date,
                                startTime = startTime,
                                endTime = endTime,
                                roomId = roomId,
                                adviserId = adviserId,
                                panelistIds = selectedPanelists.toList(),
                                status = status
                            )
                        )
                    }
                }
            }
        }
    }
}
