package com.example.thesisschedulemanagementapp.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.ui.components.common.AppButton
import com.example.thesisschedulemanagementapp.ui.models.ButtonType
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Composable
fun ActionRow(vararg actions: Triple<String, ButtonType, () -> Unit>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
        modifier = Modifier.fillMaxWidth()
    ) {
        actions.forEach { (label, type, action) ->
            AppButton(
                text = label,
                buttonType = type,
                fullWidth = false,
                modifier = Modifier.weight(1f),
                onClick = action
            )
        }
    }
}