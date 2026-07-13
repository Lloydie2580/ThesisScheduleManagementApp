package com.example.thesisschedulemanagementapp.ui.screens

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
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppTextField
import com.example.thesisschedulemanagementapp.ui.components.layout.FormPanel
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel

@Composable
fun CreateGroupScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ProfessorDashboardViewModel,
    onBack: () -> Unit
) {
    val students by viewModel.allStudents.collectAsState()
    val professors by viewModel.allProfessors.collectAsState()
    val message by viewModel.message.collectAsState()

    var researchTitle by remember { mutableStateOf("") }
    var program by remember { mutableStateOf("") }
    val selectedMembers = remember { mutableStateListOf<Int>() }
    val selectedPanelists = remember { mutableStateListOf<Int>() }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCreationOptions()
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.setMessage(null)
        }
    }

    ScreenScaffold(snackbarHostState) {
        BackHeader("Create Advisee Group", onBack)

        FormPanel(
            title = "Group Details",
            subtitle = "Fill in the research title, program, and assign members/panelists."
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {

                AppTextField(
                    value = researchTitle,
                    onValueChange = { researchTitle = it },
                    label = "Thesis Title"
                )

                AppTextField(
                    value = program,
                    onValueChange = { program = it },
                    label = "Program (e.g., CS, IT)"
                )

                Text(
                    text = "Select Members",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {
                    students.forEach { student ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedMembers.contains(student.userId),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) selectedMembers.add(student.userId) else selectedMembers.remove(student.userId)
                                }
                            )
                            Text(student.fullName ?: "Unknown Student")
                        }
                    }
                }

                Text(
                    text = "Select Panelists",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {
                    professors.filter { it.userId != user?.userId }.forEach { professor ->
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
                    text = "Create Group",
                    loading = loading,
                    buttonType = ButtonType.PRIMARY,
                    onClick = {
                        if (researchTitle.isBlank() || program.isBlank() || selectedMembers.isEmpty() || selectedPanelists.isEmpty()) {
                            viewModel.setMessage("Please complete all fields.")
                        } else {
                            loading = true
                            viewModel.createGroup(
                                researchTitle = researchTitle,
                                adviserId = user?.userId ?: 0,
                                memberIds = selectedMembers.toList(),
                                panelistIds = selectedPanelists.toList(),
                                program = program,
                                onSuccess = {
                                    loading = false
                                    onBack()
                                },
                                onError = { error ->
                                    loading = false
                                    viewModel.setMessage(error)
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}