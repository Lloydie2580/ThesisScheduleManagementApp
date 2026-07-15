package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.Success
import com.example.thesisschedulemanagementapp.data.util.PasswordRequirement

@Composable
fun PasswordChecklist(requirements: List<PasswordRequirement>) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)) {
        requirements.forEach { requirement ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (requirement.satisfied) Icons.Default.Check else Icons.Default.Circle,
                    contentDescription = null,
                    tint = if (requirement.satisfied)
                        Success
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = requirement.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (requirement.satisfied)
                        Success
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = Dimens.SpaceXS)
                )
            }
        }
    }
}