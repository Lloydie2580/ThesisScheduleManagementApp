package com.example.thesisschedulemanagementapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import androidx.activity.SystemBarStyle
import com.example.thesisschedulemanagementapp.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {

            ThesisScheduleManagementTheme {

                AppNavigation()

            }
        }
    }
}
