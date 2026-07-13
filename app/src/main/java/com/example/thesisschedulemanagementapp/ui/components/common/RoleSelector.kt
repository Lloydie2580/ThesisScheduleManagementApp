package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.SurfaceVariant

@Composable
fun RoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceVariant)
            .padding(4.dp)
    ) {
        listOf("Student", "Professor").forEach { role ->
            val selected = selectedRole.equals(role, ignoreCase = true)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else androidx.compose.ui.graphics.Color.Transparent
                    )
                    .clickable { onRoleSelected(role) }
                    .padding(vertical = Dimens.SpaceS),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = role,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}