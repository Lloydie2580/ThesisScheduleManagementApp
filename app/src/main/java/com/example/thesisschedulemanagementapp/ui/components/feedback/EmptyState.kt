package com.example.thesisschedulemanagementapp.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun EmptyState(

    title: String,

    subtitle: String,

    icon: ImageVector = Icons.Outlined.EventBusy

) {

    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Icon(

            imageVector = icon,

            contentDescription = null

        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium
        )

    }

}