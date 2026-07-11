package com.example.thesisschedulemanagementapp.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
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
    var researchTitle by remember(schedule) { mutableStateOf(schedule?.researchTitle ?: "") }
    var date by remember(schedule) { mutableStateOf(schedule?.defenseDate ?: "") }
    var startTime by remember(schedule) { mutableStateOf(schedule?.startTime ?: "") }
    var endTime by remember(schedule) { mutableStateOf(schedule?.endTime ?: "") }
    var roomId by remember(schedule) { mutableStateOf(schedule?.roomId ?: 0) }
    var status by remember(schedule) { mutableStateOf(schedule?.status ?: "Pending") }

    val selectedPanelists = remember(schedule) {
        mutableStateListOf<Int>().apply {
            addAll(schedule?.panelists?.map { it.userId }.orEmpty())
        }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModel.clearMessage()
            if (it.success) onBack()
        }
    }

    ScreenScaffold(snackbarHostState) {

        BackHeader(title, onBack)

        FormPanel(
            title = "Defense Details",
            subtitle = "Fill in the schedule information below."
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {

                PickerField(
                    label = "Group",
                    selectedKey = groupId,
                    items = groups,
                    key = { it.groupId },
                    text = { it.groupCode }
                ) {
                    groupId = it
                    groups.firstOrNull { group -> group.groupId == it }?.let { group ->
                        researchTitle = group.researchTitle
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

                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    TimePickerField(
                        label = "Start Time",
                        value = startTime,
                        onValueChange = {
                            startTime = it
                            viewModel.calculateEndTime(it)?.let { result -> endTime = result }
                        }
                    )
                }

                TimePickerField(
                    label = "End Time",
                    value = endTime,
                    onValueChange = { endTime = it }
                )

                PickerField(
                    label = "Room",
                    selectedKey = roomId,
                    items = viewModel.rooms,
                    key = { it.roomId },
                    text = { it.roomName }
                ) {
                    roomId = it
                }

                PickerField(
                    label = "Status",
                    selectedKey = status,
                    items = viewModel.statuses,
                    key = { it },
                    text = { it }
                ) {
                    status = it
                }

                Text(
                    text = "Panelists",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {
                    professors
                        .filter { it.userId != user?.userId }
                        .forEach { professor ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = selectedPanelists.contains(professor.userId),
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            if (selectedPanelists.size < 2) {
                                                selectedPanelists.add(professor.userId)
                                            } else {
                                                viewModel.setMessage("Only two panelists are allowed.")
                                            }
                                        } else {
                                            selectedPanelists.remove(professor.userId)
                                        }
                                    }
                                )
                                Text(professor.fullName)
                            }
                        }
                }

                AppButton(
                    text = if (schedule == null) "Create Schedule" else "Save Changes",
                    loading = loading,
                    buttonType = ButtonType.PRIMARY,
                    onClick = {
                        val validation = viewModel.validate(
                            groupId, researchTitle, date, startTime, endTime, roomId, selectedPanelists
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
                                    adviserId = user?.userId ?: 0,
                                    panelistIds = selectedPanelists.toList(),
                                    status = status
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}