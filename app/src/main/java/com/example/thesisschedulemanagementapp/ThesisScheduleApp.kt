package com.example.thesisschedulemanagementapp

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.thesisschedulemanagementapp.viewmodel.AuthViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ProfessorDashboardViewModel
import com.example.thesisschedulemanagementapp.viewmodel.ScheduleManagementViewModel
import com.example.thesisschedulemanagementapp.viewmodel.StudentDashboardViewModel

private enum class AppRoute {
    Login, Signup, StudentDashboard, ProfessorDashboard, StudentSchedule, ProfessorSchedules,
    CreateSchedule, UpdateSchedule, Notifications, CreateGroup
}

@Composable
fun ThesisScheduleApp() {
    val context = LocalContext.current
    val authViewModel = rememberViewModel<AuthViewModel> {
        AuthViewModel(context.applicationContext as android.app.Application)
    }
    val studentViewModel = rememberViewModel<StudentDashboardViewModel> { StudentDashboardViewModel() }
    val professorViewModel = rememberViewModel<ProfessorDashboardViewModel> { 
        ProfessorDashboardViewModel().apply { initRepositories(context.applicationContext) }
    }
    val scheduleViewModel = rememberViewModel<ScheduleManagementViewModel> { ScheduleManagementViewModel() }

    val authState by authViewModel.state.collectAsState()
    
    // Calculate initial route based on existing session data
    val initialRoute = remember(authState.data) {
        when (authState.data?.role?.lowercase()) {
            "student" -> AppRoute.StudentDashboard
            "professor" -> AppRoute.ProfessorDashboard
            else -> AppRoute.Login
        }
    }
    
    var route by remember { mutableStateOf(initialRoute) }
    
    // Update route when auth state changes (e.g. login/logout)
    LaunchedEffect(authState.data?.userId, authState.success) {
        if (authState.success || authState.data == null) {
            route = when (authState.data?.role?.lowercase()) {
                "student" -> AppRoute.StudentDashboard
                "professor" -> AppRoute.ProfessorDashboard
                else -> AppRoute.Login
            }
        }
    }

    var selectedSchedule by remember { mutableStateOf<DefenseSchedule?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val user: User? = authState.data

    LaunchedEffect(authState.message) {
        authState.message?.let {
            snackbarHostState.showSnackbar(it)
            authViewModel.clearMessage()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
                onCreateGroup = { route = AppRoute.CreateGroup },
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
                    if (user?.role.equals("student", true)) {
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
                onCreated = {
                    professorViewModel.setMessage("Group created successfully.")
                },
                onBack = {
                    user?.let { professorViewModel.load(it.userId) }
                    route = AppRoute.ProfessorDashboard
                }
            )
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> rememberViewModel(crossinline creator: () -> T): T {
    val context = LocalContext.current
    return remember(context) {
        val activity = context as? ComponentActivity 
            ?: context.findActivity() 
            ?: throw IllegalStateException("Context must be a ComponentActivity")
        ViewModelProvider(activity, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
        })[T::class.java]
    }
}

private fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
