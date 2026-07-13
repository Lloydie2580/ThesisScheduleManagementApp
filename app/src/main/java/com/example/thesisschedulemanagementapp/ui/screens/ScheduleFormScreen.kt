package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.ScheduleRequest
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppTextField
import com.example.thesisschedulemanagementapp.ui.components.common.DatePickerField
import com.example.thesisschedulemanagementapp.ui.components.common.TimePickerField
import com.example.thesisschedulemanagementapp.ui.components.layout.FormPanel
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.components.common.PickerField
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel

@Composable
fun ScheduleFormScreen(
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

    val isStudent = remember(user) { user?.role?.trim()?.equals("student", true) == true }

    ScreenScaffold(snackbarHostState) {
        BackHeader(title, onBack)

        FormPanel(
            title = "Defense Details",
            subtitle = "Fill in the schedule information below."
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) { // Use SpaceXS (4dp) for tightest field grouping

                PickerField(
                    label = "Group Code",
                    selectedKey = groupId,
                    items = groups,
                    key = { it.groupId },
                    text = { it.groupCode.orEmpty() }
                ) { selectedGroupId ->
                    groupId = selectedGroupId
                    groups.firstOrNull { it.groupId == selectedGroupId }?.let { group ->
                        researchTitle = group.researchTitle.orEmpty()
                        groupAdviserId = group.adviserId
                        selectedPanelists.clear()
                        selectedPanelists.addAll(group.panelists?.map { p -> p.userId }.orEmpty())
                    }
                }

                AppTextField(
                    value = researchTitle,
                    onValueChange = { researchTitle = it },
                    label = "Research Title"
                )

                DatePickerField(
                    label = "Defense Date",
                    value = date,
                    onValueChange = { date = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
                ) {
                    TimePickerField(
                        label = "Start Time",
                        value = startTime,
                        onValueChange = {
                            startTime = it
                            viewModel.calculateEndTime(it)?.let { calculatedEndTime -> 
                                endTime = calculatedEndTime 
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Display End Time as a disabled AppTextField to make it clearly non-editable but visible
                    AppTextField(
                        value = endTime,
                        onValueChange = {},
                        label = "End Time (Auto)",
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    )
                }

                PickerField(
                    label = "Room (Venue)",
                    selectedKey = roomId,
                    items = viewModel.rooms,
                    key = { it.roomId },
                    text = { it.roomName }
                ) { roomId = it }

                PickerField(
                    label = "Status",
                    selectedKey = status,
                    items = viewModel.statuses,
                    key = { it },
                    text = { it }
                ) { status = it }

                Text(
                    text = "Panelists",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = Dimens.SpaceS)
                )

                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXXS)) {
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
                }

                AppButton(
                    text = if (schedule == null) {
                        if (isStudent) "Request Schedule" else "Create Schedule"
                    } else "Save Changes",
                    loading = loading,
                    buttonType = ButtonType.PRIMARY,
                    modifier = Modifier.padding(top = Dimens.SpaceM),
                    onClick = {
                        val currentUserId = user?.userId ?: 0
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
                )
            }
        }
    }
}
