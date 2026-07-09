package com.example.thesisschedulemanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.NotificationItem
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.data.model.User

@Composable
internal fun ScreenScaffold(snackbarHostState: SnackbarHostState, content: @Composable () -> Unit) {
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 960.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
internal fun FormPanel(title: String, subtitle: String, content: @Composable () -> Unit) {
    Spacer(Modifier.height(32.dp))
    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Text(subtitle, style = MaterialTheme.typography.bodyMedium)
    Spacer(Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), content = { content() })
}

@Composable
internal fun PrimaryLoadingButton(label: String, loading: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = !loading, modifier = Modifier.fillMaxWidth()) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.height(18.dp).width(18.dp), strokeWidth = 2.dp)
        } else {
            Text(label)
        }
    }
}

@Composable
internal fun DashboardHeader(title: String, user: User?, onLogout: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(user?.fullName.orEmpty(), style = MaterialTheme.typography.bodyMedium)
        }
        OutlinedButton(onClick = onLogout) { Text("Logout") }
    }
}

@Composable
internal fun BackHeader(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
internal fun ActionRow(vararg actions: Pair<String, () -> Unit>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        actions.forEach { (label, action) ->
            Button(onClick = action, modifier = Modifier.weight(1f)) { Text(label) }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun ScheduleCard(
    schedule: DefenseSchedule,
    canManage: Boolean,
    onEdit: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(schedule.researchTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Group Code: ${schedule.groupCode}")
            Text("${schedule.defenseDate} | ${schedule.startTime} - ${schedule.endTime}")
            Text("Room: ${schedule.roomName}")
            Text("Adviser: ${schedule.adviserName}")
            Text("Panelists: ${schedule.panelists.joinToString { it.fullName }.ifBlank { "Not assigned" }}")
            Text("Status: ${schedule.status}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            if (canManage) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    onEdit?.let { OutlinedButton(onClick = it) { Text("Edit") } }
                    onCancel?.let { OutlinedButton(onClick = it) { Text("Cancel") } }
                    onComplete?.let { Button(onClick = it) { Text("Complete") } }
                    onDelete?.let { OutlinedButton(onClick = it) { Text("Delete") } }
                }
            }
        }
    }
}

@Composable
internal fun GroupCard(group: StudentGroup) {
    Card {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(group.groupCode, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(group.researchTitle)
            Text("Adviser: ${group.adviserName ?: "Assigned adviser"}")
            if (group.members.isNotEmpty()) Text("Members: ${group.members.joinToString { it.fullName }}")
        }
    }
}

@Composable
internal fun NotificationList(items: List<NotificationItem>) {
    if (items.isEmpty()) {
        Text("No notifications yet.")
        return
    }
    items.forEach { item ->
        Card {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(item.message)
                Text(item.createdAt, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
internal fun <T, K> PickerField(
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
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
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
