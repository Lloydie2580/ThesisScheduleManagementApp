package com.example.thesisschedulemanagementapp.ui.navigation

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.screens.CreateGroupScreen
import com.example.thesisschedulemanagementapp.ui.screens.CreateScheduleScreen
import com.example.thesisschedulemanagementapp.ui.screens.LoginScreen
import com.example.thesisschedulemanagementapp.ui.screens.NotificationsScreen
import com.example.thesisschedulemanagementapp.ui.screens.ProfessorDashboardScreen
import com.example.thesisschedulemanagementapp.ui.screens.ProfessorScheduleListScreen
import com.example.thesisschedulemanagementapp.ui.screens.SignUpScreen
import com.example.thesisschedulemanagementapp.ui.screens.StudentDashboardScreen
import com.example.thesisschedulemanagementapp.ui.screens.StudentScheduleScreen
import com.example.thesisschedulemanagementapp.ui.screens.UpdateScheduleScreen
import com.example.thesisschedulemanagementapp.ui.navigation.AppRoute
import com.example.thesisschedulemanagementapp.viewmodel.AuthViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val authViewModel = rememberViewModel<AuthViewModel> {
        AuthViewModel(context.applicationContext as Application)
    }
    val studentViewModel = rememberViewModel<StudentDashboardViewModel> { StudentDashboardViewModel() }
    val professorViewModel = rememberViewModel<ProfessorDashboardViewModel> { ProfessorDashboardViewModel() }
    val scheduleViewModel = rememberViewModel<ScheduleManagementViewModel> { ScheduleManagementViewModel() }

    val authState by authViewModel.state.collectAsState()
    var route by remember {
        mutableStateOf(
            when (authState.data?.role?.lowercase()) {
                "student" -> AppRoute.StudentDashboard
                "professor" -> AppRoute.ProfessorDashboard
                else -> AppRoute.Login
            }
        )
    }
    var selectedSchedule by remember { mutableStateOf<DefenseSchedule?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val user: User? = authState.data

    LaunchedEffect(authState.data?.userId, authState.success) {
        authState.data?.let {
            route = if (it.role.equals("student", true)) AppRoute.StudentDashboard else AppRoute.ProfessorDashboard
        }
    }

    LaunchedEffect(authState.message) {
        authState.message?.let {
            snackbarHostState.showSnackbar(it)
            authViewModel.clearMessage()
        }
    }

    when (route) {
        AppRoute.Login -> LoginScreen(
            snackbarHostState = snackbarHostState,
            state = authState,
            onLogin = authViewModel::login,
            onSignUp = { route = AppRoute.Signup }
        )
        AppRoute.Signup -> SignUpScreen(
            snackbarHostState = snackbarHostState,
            state = authState,
            onSignUp = authViewModel::signup,
            onBack = { route = AppRoute.Login }
        )
        AppRoute.StudentDashboard -> StudentDashboardScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            viewModel = studentViewModel,
            onOpenSchedule = { route = AppRoute.StudentSchedule },
            onRequestSchedule = { route = AppRoute.CreateSchedule },
            onOpenNotifications = { route = AppRoute.Notifications },
            onLogout = {
                authViewModel.logout()
                route = AppRoute.Login
            }
        )
        AppRoute.StudentSchedule -> StudentScheduleScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            viewModel = studentViewModel,
            onBack = { route = AppRoute.StudentDashboard }
        )
        AppRoute.ProfessorDashboard -> ProfessorDashboardScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            viewModel = professorViewModel,
            scheduleViewModel = scheduleViewModel,
            onOpenSchedules = { route = AppRoute.ProfessorSchedules },
            onEditSchedule = {
                selectedSchedule = it
                route = AppRoute.UpdateSchedule
            },
            onCreateSchedule = { route = AppRoute.CreateSchedule },
            onCreateGroup = {
                route = AppRoute.CreateGroup
            },
            onOpenNotifications = { route = AppRoute.Notifications },
            onLogout = {
                authViewModel.logout()
                route = AppRoute.Login
            }
        )
        AppRoute.ProfessorSchedules -> ProfessorScheduleListScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            viewModel = professorViewModel,
            scheduleViewModel = scheduleViewModel,
            onBack = { route = AppRoute.ProfessorDashboard },
            onEdit = {
                selectedSchedule = it
                route = AppRoute.UpdateSchedule
            }
        )
        AppRoute.CreateSchedule -> CreateScheduleScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            viewModel = scheduleViewModel,
            onBack = {
                val role = user?.role?.trim()?.lowercase()
                if (role == "student") {
                    user?.let { studentViewModel.load(it.userId) }
                    route = AppRoute.StudentDashboard
                } else {
                    user?.let { professorViewModel.refreshSchedules(it.userId) }
                    route = AppRoute.ProfessorDashboard
                }
            }
        )
        AppRoute.UpdateSchedule -> UpdateScheduleScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            schedule = selectedSchedule,
            viewModel = scheduleViewModel,
            onBack = {
                user?.let { professorViewModel.refreshSchedules(it.userId) }
                route = AppRoute.ProfessorSchedules
            }
        )
        AppRoute.Notifications -> NotificationsScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            studentViewModel = studentViewModel,
            professorViewModel = professorViewModel,
            onBack = {
                route = if (user?.role.equals("student", true)) AppRoute.StudentDashboard else AppRoute.ProfessorDashboard
            }
        )
        AppRoute.CreateGroup -> CreateGroupScreen(
            snackbarHostState = snackbarHostState,
            user = user,
            viewModel = professorViewModel,
            onBack = {
                user?.let { professorViewModel.load(it.userId) }
                route = AppRoute.ProfessorDashboard
            }
        )
    }
}

@Composable
private inline fun <reified T : ViewModel> rememberViewModel(crossinline creator: () -> T): T {
    val activity = LocalActivity.current as ComponentActivity
    return remember {
        ViewModelProvider(activity, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
        })[T::class.java]
    }
}
