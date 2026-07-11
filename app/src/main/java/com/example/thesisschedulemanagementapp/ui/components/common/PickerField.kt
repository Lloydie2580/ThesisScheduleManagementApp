package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T, K> PickerField(
    label: String,
    selectedKey: K,
    items: List<T>,
    key: (T) -> K,
    text: (T) -> String,
    onSelect: (K) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = items.firstOrNull { key(it) == selectedKey }?.let(text).orEmpty()
    Column {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedText.isBlank()) label else "$label: $selectedText")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text(item)) },
                    onClick = {
                        onSelect(key(item))
                        expanded = false
                    }
                )
            }
        }
    }
}