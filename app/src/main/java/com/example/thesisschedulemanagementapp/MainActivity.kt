package com.example.thesisschedulemanagementapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme

import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThesisScheduleManagementTheme {
                ThesisScheduleApp()
            }
        }
    }
}
