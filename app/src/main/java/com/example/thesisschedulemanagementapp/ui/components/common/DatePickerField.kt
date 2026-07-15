package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.SurfaceVariant
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    value: String, 
    onValueChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val storageFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.US) }
    val displayFormat = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.US) }

    val displayText = remember(value) {
        value.takeIf { it.isNotBlank() }?.let {
            runCatching { displayFormat.format(storageFormat.parse(it)!!) }.getOrNull()
        }
    }

    PickerTrigger(
        label = label,
        value = displayText,
        icon = Icons.Default.CalendarToday,
        onClick = { showPicker = true }
    )

    if (showPicker) {
        val initialMillis = value.takeIf { it.isNotBlank() }
            ?.let { runCatching { storageFormat.parse(it)?.time }.getOrNull() }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 3)
        }
        val minDateMillis = calendar.timeInMillis

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= minDateMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onValueChange(storageFormat.format(Date(it)))
                    }
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
internal fun PickerTrigger(
    label: String,
    value: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) SurfaceVariant else SurfaceVariant.copy(alpha = 0.5f))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceS),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value ?: "Select $label",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (value != null)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 1f else 0.6f)
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.6f)
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }
}
