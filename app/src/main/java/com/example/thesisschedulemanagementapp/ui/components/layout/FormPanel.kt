package com.example.thesisschedulemanagementapp.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Composable
fun FormPanel(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    AppCard {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {
                Text(text = title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            content()
        }
    }
}