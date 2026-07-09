package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.UiState

@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState,
    state: UiState<User>,
    onLogin: (String, String) -> Unit,
    onSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    ScreenScaffold(snackbarHostState) {
        FormPanel(title = "Defense Schedule Management", subtitle = "Login with your student or professor account") {
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                password,
                { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            PrimaryLoadingButton("Login", state.loading) { onLogin(email, password) }
            OutlinedButton(onClick = onSignUp, modifier = Modifier.fillMaxWidth()) {
                Text("Create Account")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun LoginScreenPreview() {
    ThesisScheduleManagementTheme {
        LoginScreen(
            snackbarHostState = remember { SnackbarHostState() },
            state = UiState<User>(),
            onLogin = { _, _ -> },
            onSignUp = {}
        )
    }
}
