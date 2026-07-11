package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Composable
fun RoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
    ) {

        FilterChip(
            selected = selectedRole.equals("Student", ignoreCase = true),
            onClick = { onRoleSelected("Student") },
            label = {
                Text("Student")
            }
        )

        FilterChip(
            selected = selectedRole.equals("Professor", ignoreCase = true),
            onClick = { onRoleSelected("Professor") },
            label = {
                Text("Professor")
            }
        )

    }

}