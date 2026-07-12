package com.example.thesisschedulemanagementapp.ui.screens

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
    var researchTitle by remember(schedule) { mutableStateOf(schedule?.researchTitle.orEmpty()) }
    var date by remember(schedule) { mutableStateOf(schedule?.defenseDate.orEmpty()) }
    var startTime by remember(schedule) { mutableStateOf(schedule?.startTime.orEmpty()) }
    var endTime by remember(schedule) { mutableStateOf(schedule?.endTime.orEmpty()) }
    var roomId by remember(schedule) { mutableStateOf(schedule?.roomId ?: 0) }
    var status by remember(schedule) { mutableStateOf(schedule?.status.orEmpty().ifBlank { "Pending" }) }
    var groupAdviserId by remember(schedule) { mutableStateOf(schedule?.adviserId ?: 0) }

    val selectedPanelists = remember(schedule) {
        mutableStateListOf<Int>().apply { addAll(schedule?.panelists?.map { it.userId }.orEmpty()) }
    }

    // Auto-fill details if group is already known (e.g. for student with only one group)
    LaunchedEffect(groups) {
        if (groupId == 0 && groups.size == 1) {
            val group = groups.first()
            groupId = group.groupId
            researchTitle = group.researchTitle.orEmpty()
            groupAdviserId = group.adviserId
            selectedPanelists.clear()
            selectedPanelists.addAll(group.panelists?.map { it.userId }.orEmpty())
        } else if (groupId != 0 && groupAdviserId == 0) {
            groups.firstOrNull { it.groupId == groupId }?.let { group ->
                groupAdviserId = group.adviserId
                if (researchTitle.isBlank()) researchTitle = group.researchTitle.orEmpty()
            }
        }
    }

    // Memoize the filtered list to avoid recalculating on every recomposition
    val availableProfessors = remember(professors, user) {
        professors.filter { it.userId != user?.userId }
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

                PickerField("Group Code", groupId, groups, { it.groupId }, { it.groupCode.orEmpty() }) { selectedGroupId ->
                    groupId = selectedGroupId
                    groups.firstOrNull { it.groupId == selectedGroupId }?.let { group ->
                        researchTitle = group.researchTitle.orEmpty()
                        groupAdviserId = group.adviserId
                        selectedPanelists.clear()
                        selectedPanelists.addAll(group.panelists?.map { p -> p.userId }.orEmpty())
                    }
                }

                OutlinedTextField(
                    value = researchTitle,
                    onValueChange = { researchTitle = it },
                    label = { Text("Research Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (MM/DD/YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = {
                            startTime = it
                            viewModel.calculateEndTime(it)?.let { calculatedEndTime -> endTime = calculatedEndTime }
                        },
                        label = { Text("Start (H:MM AM/PM)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End (H:MM AM/PM)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                PickerField("Room", roomId, viewModel.rooms, { it.roomId }, { it.roomName }) { roomId = it }

                PickerField("Status", status, viewModel.statuses, { it }, { it }) { status = it }

                Text("Panelists", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)

                // Use the memoized list here
                availableProfessors.forEach { professor ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPanelists.contains(professor.userId),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
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
                        Text(professor.fullName ?: "Unknown Professor")
                    }
                }

                PrimaryLoadingButton(if (schedule == null) "Create Schedule" else "Save Changes", loading) {
                    val currentUserId = user?.userId ?: 0

                    // Directly use the state variables rather than creating redundant local copies
                    val validation = viewModel.validate(
                        groupId, researchTitle, date, startTime, endTime, roomId, selectedPanelists.toList()
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
                                adviserId = groupAdviserId,
                                panelistIds = selectedPanelists.toList(),
                                status = status,
                                requesterId = currentUserId,
                                requesterRole = user?.role
                            )
                        )
                    }
                }
            }
        }
    }
}