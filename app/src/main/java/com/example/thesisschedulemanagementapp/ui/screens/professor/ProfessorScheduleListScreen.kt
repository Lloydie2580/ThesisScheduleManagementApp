package com.example.thesisschedulemanagementapp.ui.screens.professor


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User


import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader

import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView

import com.example.thesisschedulemanagementapp.ui.components.headers.BackHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold

import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme


import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel



@Composable
fun ProfessorScheduleListScreen(

    snackbarHostState: SnackbarHostState,

    user: User?,

    viewModel: ProfessorDashboardViewModel,

    scheduleViewModel: ScheduleManagementViewModel,

    onBack: () -> Unit,

    onEdit: (DefenseSchedule) -> Unit

) {


    val schedules by viewModel.schedules.collectAsState()

    val message by scheduleViewModel.message.collectAsState()


    var scheduleToDelete by remember {

        mutableStateOf<DefenseSchedule?>(null)

    }



    LaunchedEffect(user?.userId) {

        user?.let {

            viewModel.load(it.userId)

        }

    }



    LaunchedEffect(message) {

        message?.let {


            snackbarHostState.showSnackbar(it.message)


            scheduleViewModel.clearMessage()


            user?.let { currentUser ->

                viewModel.refreshSchedules(
                    currentUser.userId
                )

            }

        }

    }




    ScreenScaffold(snackbarHostState) {


        BackHeader(

            title = "Professor Schedules",

            onBack = onBack

        )



        Spacer(
            modifier = Modifier.height(
                Dimens.SpaceL
            )
        )



        SectionHeader(

            title = "Defense Schedule Management",

            subtitle =
                "View and manage your assigned thesis defenses"

        )



        Spacer(
            modifier = Modifier.height(
                Dimens.SpaceM
            )
        )



        when {


            schedules.loading -> {


                LoadingView(
                    message = "Loading schedules..."
                )


            }



            schedules.data.isNullOrEmpty() -> {


                EmptyState(

                    title = "No Schedules Found",

                    message =
                        "Create your first thesis defense schedule."

                )


            }



            else -> {


                LazyColumn(

                    verticalArrangement =
                        Arrangement.spacedBy(
                            Dimens.SpaceM
                        )

                ) {


                    items(
                        schedules.data.orEmpty()
                    ) { schedule ->



                        ScheduleCard(

                            schedule = schedule,


                            canManage =
                                schedule.adviserId ==
                                        user?.userId,


                            onEdit = {

                                onEdit(schedule)

                            },


                            onCancel = {


                                user?.let {


                                    scheduleViewModel.cancel(

                                        schedule.scheduleId,

                                        it.userId

                                    )

                                }


                            },


                            onComplete = {


                                user?.let {


                                    scheduleViewModel.complete(

                                        schedule.scheduleId,

                                        it.userId

                                    )


                                }


                            },


                            onDelete = {


                                scheduleToDelete =
                                    schedule


                            }

                        )


                    }

                }


            }


        }


    }





    scheduleToDelete?.let { schedule ->


        AlertDialog(


            onDismissRequest = {

                scheduleToDelete = null

            },


            title = {

                Text(
                    "Delete Schedule"
                )

            },


            text = {

                Text(
                    "This action permanently removes the defense schedule."
                )

            },


            confirmButton = {


                TextButton(

                    onClick = {


                        user?.let {


                            scheduleViewModel.delete(

                                schedule.scheduleId,

                                it.userId

                            )

                        }


                        scheduleToDelete = null


                    }

                ) {

                    Text("Delete")

                }


            },


            dismissButton = {


                TextButton(

                    onClick = {

                        scheduleToDelete = null

                    }

                ) {

                    Text("Cancel")

                }


            }

        )


    }


}






@Preview(
    showBackground = true,
    widthDp = 390
)
@Composable
private fun ProfessorScheduleListScreenPreview() {


    ThesisScheduleManagementTheme {


        ProfessorScheduleListScreen(

            snackbarHostState =
                remember {
                    SnackbarHostState()
                },


            user = null,


            viewModel =
                ProfessorDashboardViewModel(),


            scheduleViewModel =
                ScheduleManagementViewModel(),


            onBack = {},


            onEdit = {}

        )


    }

}