package com.example.thesisschedulemanagementapp.ui.screens.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppTextField
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
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

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }


    ScreenScaffold(
        snackbarHostState
    ) {


        AppCard {


            Column(

                verticalArrangement =
                    Arrangement.spacedBy(
                        Dimens.SpaceM
                    )

            ) {


                Text(

                    text = "Thesis Defense",

                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,

                    fontWeight = FontWeight.Bold

                )


                Text(

                    text =
                        "Schedule Management System",

                    style =
                        androidx.compose.material3.MaterialTheme.typography.titleMedium

                )


                Text(

                    text =
                        "Login using your student or professor account",

                    style =
                        androidx.compose.material3.MaterialTheme.typography.bodyMedium

                )


                Spacer(
                    modifier =
                        androidx.compose.ui.Modifier
                            .height(Dimens.SpaceS)
                )


                AppTextField(

                    value = email,

                    onValueChange = {
                        email = it
                    },

                    label = "Email",

                    leadingIcon =
                        Icons.Default.Email

                )


                AppTextField(

                    value = password,

                    onValueChange = {
                        password = it
                    },

                    label = "Password",

                    leadingIcon =
                        Icons.Default.Lock,

                    isPassword =
                        !passwordVisible,


                    trailingIcon = {

                        IconButton(

                            onClick = {
                                passwordVisible =
                                    !passwordVisible
                            }

                        ) {

                            Icon(

                                imageVector =
                                    if(passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,

                                contentDescription =
                                    "Toggle password visibility"

                            )

                        }

                    }

                )


                AppButton(

                    text = "Login",

                    loading = state.loading,

                    buttonType =
                        ButtonType.PRIMARY

                ){

                    onLogin(
                        email,
                        password
                    )

                }


                OutlinedButton(

                    onClick = onSignUp

                ){

                    Text(
                        "Create Account"
                    )

                }

            }

        }

    }

}