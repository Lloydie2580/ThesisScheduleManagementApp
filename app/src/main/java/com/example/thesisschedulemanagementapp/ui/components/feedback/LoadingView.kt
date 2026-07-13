package com.example.thesisschedulemanagementapp.ui.components.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Composable
fun LoadingView(
    message: String = "Loading..."
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.SpaceXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}