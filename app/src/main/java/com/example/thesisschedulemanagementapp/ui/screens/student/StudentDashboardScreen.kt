package com.example.thesisschedulemanagementapp.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.cards.ScheduleCard
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.components.cards.NotificationList
import com.example.thesisschedulemanagementapp.ui.components.headers.DashboardHeader
import com.example.thesisschedulemanagementapp.ui.components.layout.ScreenScaffold
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun StudentDashboardScreen(
    snackbarHostState: SnackbarHostState,
    user: User?,
    viewModel: StudentDashboardViewModel,
    onOpenSchedule: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLogout: () -> Unit
) {

    val scheduleState by viewModel.schedule.collectAsState()
    val notificationState by viewModel.notifications.collectAsState()

    LaunchedEffect(user?.userId) {
        user?.let {
            viewModel.load(it.userId)
        }
    }

    ScreenScaffold(snackbarHostState) {

        DashboardHeader(
            title = "Student Dashboard",
            user = user,
            onLogout = onLogout
        )

        Spacer(
            modifier = Modifier.height(Dimens.SpaceS)
        )

        AppButton(
            text = "View My Schedule",
            icon = Icons.Default.CalendarMonth,
            buttonType = ButtonType.PRIMARY,
            onClick = onOpenSchedule
        )

        AppButton(
            text = "Notifications",
            icon = Icons.Default.Notifications,
            buttonType = ButtonType.OUTLINED,
            onClick = onOpenNotifications
        )

        Spacer(
            modifier = Modifier.height(Dimens.SpaceM)
        )

        Text(
            text = "Upcoming Defense",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        when {

            scheduleState.loading -> {

                CircularProgressIndicator()

            }

            scheduleState.data != null -> {

                ScheduleCard(
                    schedule = scheduleState.data!!
                )

            }

            else -> {

                AppCard {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
                    ) {

                        Text(
                            text = "No Schedule Yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Your adviser has not assigned a thesis defense schedule yet."
                        )

                    }

                }

            }

        }

        Spacer(
            modifier = Modifier.height(Dimens.SpaceL)
        )

        Text(
            text = "Recent Updates",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        NotificationList(
            items = notificationState.data.orEmpty().take(3)
        )

    }

}