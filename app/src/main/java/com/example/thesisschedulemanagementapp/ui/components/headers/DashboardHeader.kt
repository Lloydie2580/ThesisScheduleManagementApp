package com.example.thesisschedulemanagementapp.ui.components.headers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import java.util.Calendar

@Composable
fun DashboardHeader(
    title: String,
    user: User?,
    onLogout: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = greeting(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = user?.fullName ?: "Guest",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            AppButton(
                text = "Logout",
                icon = Icons.Outlined.Logout,
                buttonType = ButtonType.OUTLINED,
                fullWidth = false,
                onClick = onLogout
            )

        }

        HorizontalDivider(
            modifier = Modifier.padding(top = Dimens.SpaceS)
        )

    }

}

private fun greeting(): String {

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    return when {

        hour < 12 -> "Good Morning 👋"

        hour < 18 -> "Good Afternoon ☀️"

        else -> "Good Evening 🌙"

    }

}