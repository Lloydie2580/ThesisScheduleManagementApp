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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    label: String,
    value: String, // stored as "h:mm a", empty if unset
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    val showPicker = remember { mutableStateOf(false) }

    val displayText = remember(value) {
        value.takeIf { it.isNotBlank() }
    }

    PickerTrigger(
        label = label,
        value = displayText,
        icon = Icons.Default.AccessTime,
        onClick = { if (!readOnly) showPicker.value = true },
        modifier = modifier
    )

    if (showPicker.value && !readOnly) {
        // Robust parsing for initial state
        val formatter = remember { SimpleDateFormat("h:mm a", Locale.US) }
        val initialTime = remember(value) {
            val calendar = Calendar.getInstance()
            value.takeIf { it.isNotBlank() }?.let { v ->
                runCatching { formatter.parse(v.uppercase(Locale.US)) }.getOrNull()
            }?.let {
                calendar.time = it
                calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
            } ?: (9 to 0)
        }

        val state = rememberTimePickerState(
            initialHour = initialTime.first,
            initialMinute = initialTime.second,
            is24Hour = false
        )

        Dialog(onDismissRequest = { showPicker.value = false }) {
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
                        TextButton(onClick = { showPicker.value = false }) { Text("Cancel") }
                        TextButton(onClick = {
                            val cal = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, state.hour)
                                set(Calendar.MINUTE, state.minute)
                            }
                            onValueChange(formatter.format(cal.time))
                            showPicker.value = false
                        }) { Text("OK") }
                    }
                }
            }
        }
    }
}
