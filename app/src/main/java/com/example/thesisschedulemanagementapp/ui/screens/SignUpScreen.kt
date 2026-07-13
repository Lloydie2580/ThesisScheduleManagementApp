package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppTextField
import com.example.thesisschedulemanagementapp.ui.components.common.RoleSelector
import com.example.thesisschedulemanagementapp.ui.components.headers.AuthHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.FormPanel
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
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
    var passwordVisible by remember { mutableStateOf(false) }

    val valid =
        fullName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                role.isNotBlank()

    ScreenScaffold(snackbarHostState) {

        AuthHeader(
            title = "Create Account",
            subtitle = "Join the Thesis Schedule Management System"
        )

        FormPanel(
            title = "Registration",
            subtitle = "Create your student or professor account."
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {

                AppTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    leadingIcon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

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
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                )

                RoleSelector(
                    selectedRole = role,
                    onRoleSelected = { role = it }
                )

                AppButton(
                    text = "Create Account",
                    enabled = valid,
                    loading = state.loading,
                    buttonType = ButtonType.PRIMARY,
                    onClick = {
                        onSignUp(
                            fullName.trim(),
                            email.trim(),
                            password,
                            role
                        )
                    }
                )

                AppButton(
                    text = "Back to Login",
                    buttonType = ButtonType.OUTLINED,
                    onClick = onBack
                )
            }
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