package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.components.models.ButtonType

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.PRIMARY,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = true
) {
    val finalModifier =
        if (fullWidth)
            modifier.fillMaxWidth().height(Dimens.ButtonHeight)
        else
            modifier.height(Dimens.ButtonHeight)

    when (buttonType) {

        ButtonType.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = finalModifier,
                enabled = enabled && !loading,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) { ButtonContent(text, icon, loading, MaterialTheme.colorScheme.primary) }
        }

        else -> {
            val containerColor = when (buttonType) {
                ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
                ButtonType.SECONDARY -> MaterialTheme.colorScheme.secondary
                ButtonType.DANGER -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.primary
            }

            Button(
                onClick = onClick,
                modifier = finalModifier,
                enabled = enabled && !loading,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) { ButtonContent(text, icon, loading, MaterialTheme.colorScheme.onPrimary) }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: ImageVector?,
    loading: Boolean,
    contentColor: androidx.compose.ui.graphics.Color
) {
    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            color = contentColor,
            strokeWidth = 2.dp
        )
        return
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = null, tint = contentColor)
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge, color = contentColor)
    }
}