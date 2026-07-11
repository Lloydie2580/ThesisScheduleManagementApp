package com.example.thesisschedulemanagementapp.ui.screens.professor

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.shared.ScheduleFormScreen
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel


@Composable
fun UpdateScheduleScreen(

    snackbarHostState: SnackbarHostState,

    user: User?,

    schedule: DefenseSchedule?,

    viewModel: ScheduleManagementViewModel,

    onBack: () -> Unit

) {


    LaunchedEffect(user?.userId) {

        user?.let {

            viewModel.loadOptions(
                it.userId
            )

        }

    }



    ScheduleFormScreen(

        title = "Update Defense Schedule",

        snackbarHostState = snackbarHostState,

        user = user,

        viewModel = viewModel,

        schedule = schedule,

        onSubmit = viewModel::update,

        onBack = onBack

    )

}



@Preview(
    showBackground = true,
    widthDp = 390
)
@Composable
private fun UpdateScheduleScreenPreview() {

    ThesisScheduleManagementTheme {

        UpdateScheduleScreen(

            snackbarHostState =
                remember {
                    SnackbarHostState()
                },

            user = null,

            schedule = null,

            viewModel =
                ScheduleManagementViewModel(),

            onBack = {}

        )

    }

}