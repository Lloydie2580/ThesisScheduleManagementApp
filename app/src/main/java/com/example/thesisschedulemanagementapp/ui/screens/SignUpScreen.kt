package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.UiState

@Composable
fun SignUpScreen(
    snackbarHostState: SnackbarHostState,
    state: UiState<User>,
    onSignUp: (String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    ScreenScaffold(snackbarHostState) {
        FormPanel(title = "Account Creation", subtitle = "Select one account role") {
            OutlinedTextField(fullName, { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                password,
                { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = role == "Student", onClick = { role = "Student" }, label = { Text("Student") })
                FilterChip(selected = role == "Professor", onClick = { role = "Professor" }, label = { Text("Professor") })
            }
            PrimaryLoadingButton("Sign Up", state.loading) { onSignUp(fullName, email, password, role) }
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to Login") }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun SignUpScreenPreview() {
    ThesisScheduleManagementTheme {
        SignUpScreen(
            snackbarHostState = remember { SnackbarHostState() },
            state = UiState<User>(),
            onSignUp = { _, _, _, _ -> },
            onBack = {}
        )
    }
}
