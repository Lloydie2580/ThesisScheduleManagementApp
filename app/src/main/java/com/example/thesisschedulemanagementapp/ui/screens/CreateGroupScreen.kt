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
import com.example.thesisschedulemanagementapp.data.model.User
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
                BackHeader("Create Advisee Group", onBack)
                OutlinedTextField(researchTitle, { researchTitle = it }, label = { Text("Thesis Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(program, { program = it }, label = { Text("Program (e.g., CS, IT)") }, modifier = Modifier.fillMaxWidth())
                
                Text("Select Members", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                students.forEach { student ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedMembers.contains(student.userId),
                            onCheckedChange = { if (it) selectedMembers.add(student.userId) else selectedMembers.remove(student.userId) }
                        )
                        Text(student.fullName ?: "Unknown Student")
                    }
                }

                Text("Select Panelists", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                professors.filter { it.userId != user?.userId }.forEach { professor ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPanelists.contains(professor.userId),
                            onCheckedChange = { 
                                if (it) {
                                    if (selectedPanelists.size < 2) selectedPanelists.add(professor.userId)
                                } else {
                                    selectedPanelists.remove(professor.userId)
                                }
                            }
                        )
                        Text(professor.fullName ?: "Unknown Professor")
                    }
                }

                PrimaryLoadingButton("Create Group", loading) {
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
                                onBack() // This directs back to dashboard
                            },
                            onError = { error ->
                                loading = false
                                viewModel.setMessage(error)
                            }
                        )
                    }
                }
            }
        }
    }
}
