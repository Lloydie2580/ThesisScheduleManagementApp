package com.example.thesisschedulemanagementapp.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionRow(vararg actions: Pair<String, () -> Unit>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        actions.forEach { (label, action) ->
            Button(
                onClick = action,
                modifier = Modifier.weight(1f)
            ) {
                Text(label)
            }
        }
    }
}