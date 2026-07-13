package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppTextField
import com.example.thesisschedulemanagementapp.ui.components.headers.AuthHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.FormPanel
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
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
    var passwordVisible by remember { mutableStateOf(false) }

    val isValid = email.isNotBlank() && password.isNotBlank()

    ScreenScaffold(snackbarHostState) {

        AuthHeader(
            title = "Thesis Defense",
            subtitle = "Schedule Management System"
        )

        FormPanel(
            title = "Welcome Back",
            subtitle = "Login using your student or professor account."
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {

                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = !passwordVisible,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                )

                AppButton(
                    text = "Login",
                    loading = state.loading,
                    enabled = isValid,
                    buttonType = ButtonType.PRIMARY,
                    onClick = {
                        onLogin(email.trim(), password)
                    }
                )

                AppButton(
                    text = "Create Account",
                    buttonType = ButtonType.OUTLINED,
                    onClick = onSignUp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    ThesisScheduleManagementTheme {
        LoginScreen(
            snackbarHostState = remember { SnackbarHostState() },
            state = UiState(),
            onLogin = { _, _ -> },
            onSignUp = {}
        )
    }
}