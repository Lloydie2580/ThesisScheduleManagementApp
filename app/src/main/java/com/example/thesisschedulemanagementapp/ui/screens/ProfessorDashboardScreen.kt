package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.calendar.DashboardCalendar
import com.example.thesisschedulemanagementapp.ui.components.cards.GroupCard
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.SectionHeader
import com.example.thesisschedulemanagementapp.ui.components.feedback.EmptyState
import com.example.thesisschedulemanagementapp.ui.components.feedback.LoadingView
import com.example.thesisschedulemanagementapp.ui.components.headers.DashboardHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@Composable
fun ProfessorDashboardScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: ProfessorDashboardViewModel,
    scheduleViewModel: ScheduleManagementViewModel,
    onOpenSchedules: () -> Unit,
    onEditSchedule: (DefenseSchedule) -> Unit,
    onCreateSchedule: () -> Unit,
    onCreateGroup: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLogout: () -> Unit
) {
    val schedulesState by viewModel.schedules.collectAsState()
    val groupsState by viewModel.groups.collectAsState()
    val message by scheduleViewModel.message.collectAsState()

    var scheduleFilter by remember {
        mutableStateOf("All")
    }
    var expandedFilter by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(user?.userId) {
        user?.let { viewModel.load(it.userId) }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            scheduleViewModel.clearMessage()
            user?.let { u -> viewModel.refreshSchedules(u.userId) }
        }
    }

    ScreenScaffold(snackbarHostState) {
        DashboardHeader(
            title = "Professor Dashboard",
            user = user,
            onLogout = onLogout,
            onNotifications = onOpenNotifications
        )

        SectionHeader(
            title = "Defense Calendar",
            subtitle = "View your scheduled defenses"
        )


        DashboardCalendar(
            schedules = schedulesState.data ?: emptyList(),
            user = user,
            onEditSchedule = onEditSchedule
        )

        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {


            AppButton(
                text = "Manage Schedules",
                icon = Icons.Default.CalendarMonth,
                buttonType = ButtonType.OUTLINED,
                onClick = onOpenSchedules
            )

            AppButton(
                text = "Create Group",
                buttonType = ButtonType.OUTLINED,
                onClick = onCreateGroup
            )
        }

        SectionHeader(
            title = "Advisee Groups",
            subtitle = "Student groups assigned under your supervision"
        )

        when {
            groupsState.loading -> LoadingView("Loading groups...")
            groupsState.data.isNullOrEmpty() -> EmptyState(
                title = "No Advisee Groups",
                message = "No thesis groups are currently assigned to you."
            )
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    groupsState.data!!.forEach { group ->
                        GroupCard(group)
                    }
                }
            }
        }

        if (!schedulesState.loading && !schedulesState.data.isNullOrEmpty()) {
            val allSchedules = schedulesState.data!!
            val adviseeSchedules = allSchedules.filter { it.adviserId == user?.userId }
            val panelistSchedules = allSchedules.filter { it.adviserId != user?.userId }

            val displayedSchedules = when(scheduleFilter) {
                "My Advisees" -> adviseeSchedules
                "Panelist Assignments" -> panelistSchedules
                else -> allSchedules
            }

            SectionHeader(
                title = "Defense Schedules",
                subtitle = "Manage your advisee and panelist assignments"
            )

            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedFilter = true
                        }
                        .padding(Dimens.SpaceS),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = scheduleFilter,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expandedFilter,
                    onDismissRequest = {
                        expandedFilter = false
                    }
                ) {
                    listOf(
                        "All",
                        "My Advisees",
                        "Panelist Assignments"
                    ).forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                scheduleFilter = option
                                expandedFilter = false
                            }
                        )
                    }
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {
                items(displayedSchedules) { schedule ->

                    val isMyAdvisee = schedule.adviserId == user?.userId

                    val myPanelistInfo = schedule.panelists
                        ?.find { it.userId == user?.userId }

                    val hasApproved = myPanelistInfo?.isApproved == true

                    ScheduleCard(
                        modifier = Modifier.width(320.dp),
                        schedule = schedule,
                        canManage = isMyAdvisee,
                        currentUserId = user?.userId,

                        onEdit = if (isMyAdvisee) {
                            {
                                onEditSchedule(schedule)
                            }
                        } else null,

                        onCancel = if (isMyAdvisee) {
                            {
                                user?.let {
                                    scheduleViewModel.cancel(
                                        schedule.scheduleId,
                                        it.userId
                                    )
                                }
                            }
                        } else null,

                        onDelete = if (isMyAdvisee) {
                            {
                                user?.let {
                                    scheduleViewModel.delete(
                                        schedule.scheduleId,
                                        it.userId
                                    )
                                }
                            }
                        } else null,

                        onApprove = if (!isMyAdvisee && !hasApproved) {
                            {
                                user?.let {
                                    scheduleViewModel.approve(
                                        schedule.scheduleId,
                                        it.userId,
                                        it.fullName ?: "Professor"
                                    )
                                }
                            }
                        } else null,

                        onRetractApproval = if (!isMyAdvisee && hasApproved) {
                            {
                                user?.let {
                                    scheduleViewModel.cancelApproval(
                                        schedule.scheduleId,
                                        it.userId,
                                        it.fullName ?: "Professor"
                                    )
                                }
                            }
                        } else null,

                        onReject = if (!isMyAdvisee && !hasApproved) {
                            {
                                user?.let {
                                    scheduleViewModel.reject(
                                        schedule.scheduleId,
                                        it.userId
                                    )
                                }
                            }
                        } else null
                    )
                }
            }
        } else if (schedulesState.loading) {
            LoadingView("Loading schedules...")
        } else {
            SectionHeader(title = "Assigned Schedules")
            EmptyState(title = "No Schedules", message = "No upcoming defenses found.")
        }
    }
}
