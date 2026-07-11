package com.example.thesisschedulemanagementapp.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.NotificationItem

@Composable
fun NotificationList(items: List<NotificationItem>) {
    if (items.isEmpty()) {
        Text("No notifications yet.")
        return
    }
    items.forEach { item ->
        Card {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(item.message)
                Text(item.createdAt, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}