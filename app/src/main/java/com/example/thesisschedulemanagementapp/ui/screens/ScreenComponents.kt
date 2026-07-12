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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.DefenseSchedule
import com.example.thesisschedulemanagementapp.data.model.NotificationItem
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.data.model.User

@Composable
internal fun SectionHeader(
    title: String,
    onAction: (() -> Unit)? = null,
    actionLabel: String? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (onAction != null && actionLabel != null) {
            TextButton(onClick = onAction) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
internal fun ApprovalBadge(name: String?, approved: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
        Icon(
            imageVector = if (approved) Icons.Default.CheckCircle else Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = if (approved) Color(0xFF4CAF50) else Color(0xFFFFA000)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = name.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = if (approved) Color(0xFF4CAF50) else Color(0xFF757575),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun HorizontalScrollSection(
    title: String,
    onAction: (() -> Unit)? = null,
    actionLabel: String? = null,
    content: @Composable () -> Unit
) {
    Column {
        SectionHeader(title, onAction, actionLabel)
        Box(contentAlignment = Alignment.CenterStart) {
            content()
        }
    }
}

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
    currentUserId: Int? = null,
    onEdit: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onApprove: (() -> Unit)? = null,
    onRetractApproval: (() -> Unit)? = null,
    onReject: (() -> Unit)? = null
) {
    val isAdviser = schedule.adviserId == currentUserId
    val currentPanelist = schedule.panelists?.firstOrNull { it.userId == currentUserId }
    val hasApproved = if (isAdviser) schedule.adviserApproved else currentPanelist?.isApproved == true

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.width(340.dp) // Restored size
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text(
                    schedule.researchTitle.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (canManage && onEdit != null) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
            }
            Text("Group Code: ${schedule.groupCode.orEmpty()}", style = MaterialTheme.typography.bodySmall)
            Text("${schedule.defenseDate.orEmpty()} | ${schedule.startTime.orEmpty()} - ${schedule.endTime.orEmpty()}", style = MaterialTheme.typography.bodySmall)
            Text("Room: ${schedule.roomName.orEmpty()}", style = MaterialTheme.typography.bodySmall)
            
            Text("Approvals:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            FlowRow {
                ApprovalBadge(schedule.adviserName, schedule.adviserApproved)
                schedule.panelists?.forEach { panelist ->
                    ApprovalBadge(panelist.fullName, panelist.isApproved)
                }
            }
            
            Text("Status: ${schedule.status.orEmpty()}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            
            if (canManage || onApprove != null || onReject != null || onRetractApproval != null) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (canManage) {
                        onCancel?.let { OutlinedButton(onClick = it, contentPadding = ButtonDefaults.TextButtonContentPadding) { Text("Cancel Schedule", style = MaterialTheme.typography.labelSmall) } }
                        onComplete?.let { Button(onClick = it, contentPadding = ButtonDefaults.TextButtonContentPadding) { Text("Complete", style = MaterialTheme.typography.labelSmall) } }
                        onDelete?.let { OutlinedButton(onClick = it, contentPadding = ButtonDefaults.TextButtonContentPadding) { Text("Delete", style = MaterialTheme.typography.labelSmall) } }
                    }
                    
                    if (schedule.status == "Pending" || schedule.status == "Scheduled") {
                        if (hasApproved) {
                            onRetractApproval?.let { OutlinedButton(onClick = it, contentPadding = ButtonDefaults.TextButtonContentPadding) { Text("Cancel Approval", style = MaterialTheme.typography.labelSmall) } }
                        } else if (onApprove != null || onReject != null) {
                            onApprove?.let { Button(onClick = it, contentPadding = ButtonDefaults.TextButtonContentPadding) { Text("Approve", style = MaterialTheme.typography.labelSmall) } }
                            onReject?.let { OutlinedButton(onClick = it, contentPadding = ButtonDefaults.TextButtonContentPadding) { Text("Reject", style = MaterialTheme.typography.labelSmall) } }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun GroupCard(group: StudentGroup) {
    Card(
        modifier = Modifier.width(300.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(group.groupCode.orEmpty(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(group.researchTitle.orEmpty(), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Adviser: ${group.adviserName ?: "Assigned adviser"}", style = MaterialTheme.typography.bodySmall)
            if (!group.members.isNullOrEmpty()) {
                Text("Members: ${group.members.joinToString { it.fullName.orEmpty() }}", style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
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
