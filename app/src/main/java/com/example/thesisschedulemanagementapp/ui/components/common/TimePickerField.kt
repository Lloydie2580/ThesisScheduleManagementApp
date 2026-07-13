package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    label: String,
    value: String, // stored as "HH:mm" 24hr, empty if unset
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    val displayText = remember(value) {
        value.takeIf { it.isNotBlank() }?.let { formatTimeDisplay(it) }
    }

    PickerTrigger(
        label = label,
        value = displayText,
        icon = Icons.Default.AccessTime,
        onClick = { showPicker = true }
    )

    if (showPicker) {
        val (initHour, initMinute) = value.takeIf { it.isNotBlank() }
            ?.split(":")
            ?.let { it[0].toIntOrNull() to it[1].toIntOrNull() }
            ?.let { (h, m) -> (h ?: 9) to (m ?: 0) }
            ?: (9 to 0)

        val state = rememberTimePickerState(
            initialHour = initHour,
            initialMinute = initMinute,
            is24Hour = false
        )

        Dialog(onDismissRequest = { showPicker = false }) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.SpaceL),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = Dimens.SpaceM)
                    )

                    TimePicker(state = state)

                    Row(
                        modifier = Modifier.padding(top = Dimens.SpaceM),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
                    ) {
                        TextButton(onClick = { showPicker = false }) { Text("Cancel") }
                        TextButton(onClick = {
                            val h = state.hour.toString().padStart(2, '0')
                            val m = state.minute.toString().padStart(2, '0')
                            onValueChange("$h:$m")
                            showPicker = false
                        }) { Text("OK") }
                    }
                }
            }
        }
    }
}

private fun formatTimeDisplay(value: String): String? {
    val parts = value.split(":")
    val h = parts.getOrNull(0)?.toIntOrNull() ?: return null
    val m = parts.getOrNull(1)?.toIntOrNull() ?: return null
    val period = if (h < 12) "AM" else "PM"
    val hour12 = when {
        h == 0 -> 12
        h > 12 -> h - 12
        else -> h
    }
    return "$hour12:${m.toString().padStart(2, '0')} $period"
}