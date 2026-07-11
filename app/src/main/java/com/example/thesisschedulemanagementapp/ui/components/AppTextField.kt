package com.example.thesisschedulemanagementapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    readOnly: Boolean = false
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        enabled = enabled,
        readOnly = readOnly,
        label = {
            Text(label)
        },

        leadingIcon = leadingIcon?.let {

            {
                androidx.compose.material3.Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }

        },

        trailingIcon = trailingIcon,

        visualTransformation =
            if (isPassword)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,

        shape = MaterialTheme.shapes.medium,

        colors = OutlinedTextFieldDefaults.colors()
    )

}