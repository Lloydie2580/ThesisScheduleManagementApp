package com.example.thesisschedulemanagementapp.ui.screens.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppTextField
import com.example.thesisschedulemanagementapp.ui.components.common.RoleSelector
import com.example.thesisschedulemanagementapp.ui.components.headers.AuthHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.layout.FormPanel
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.UiState


@Composable
fun SignUpScreen(

    snackbarHostState: SnackbarHostState,

    state: UiState<User>,

    onSignUp: (
        String,
        String,
        String,
        String
    ) -> Unit,

    onBack: () -> Unit

) {


    var fullName by remember {
        mutableStateOf("")
    }


    var email by remember {
        mutableStateOf("")
    }


    var password by remember {
        mutableStateOf("")
    }


    var role by remember {
        mutableStateOf("")
    }


    var passwordVisible by remember {
        mutableStateOf(false)
    }



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
                    label = "Full Name"
                )

                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingIcon = Icons.Default.Email
                )

                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = !passwordVisible,
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
                    onRoleSelected = {
                        role = it
                    }
                )

                val valid =
                    fullName.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            role.isNotBlank()

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