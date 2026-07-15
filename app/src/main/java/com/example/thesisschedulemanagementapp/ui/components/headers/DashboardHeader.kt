package com.example.thesisschedulemanagementapp.ui.components.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.User
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.Primary
import com.example.thesisschedulemanagementapp.ui.theme.Secondary
import com.example.thesisschedulemanagementapp.ui.theme.SurfaceVariant
import java.util.Calendar

@Composable
fun DashboardHeader(
    title: String,
    user: User?,
    onLogout: () -> Unit,
    onNotifications: (() -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = greeting(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    onNotifications?.let {
                        HeaderIconButton(
                            icon = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            onClick = it
                        )
                    }

                    HeaderIconButton(
                        icon = Icons.Outlined.Logout,
                        contentDescription = "Logout",
                        onClick = onLogout
                    )
                }
            }

            Text(
                text = user?.fullName?.substringBefore(" ") ?: "Guest",
                style = MaterialTheme.typography.displayMedium.copy(
                    brush = Brush.linearGradient(listOf(Primary, Secondary))
                ),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.offset(y = (-8).dp)
        )
    }
}

@Composable
private fun HeaderIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(SurfaceVariant)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun greeting(): String {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning,"
        in 12..16 -> "Good Afternoon,"
        else -> "Good Evening,"
    }
}