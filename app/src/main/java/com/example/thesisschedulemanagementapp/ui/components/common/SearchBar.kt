package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search..."
) {

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = Modifier.fillMaxWidth(),

        singleLine = true,

        placeholder = {
            Text(
                text = placeholder
            )
        },

        leadingIcon = {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )

        },

        shape = MaterialTheme.shapes.medium

    )

}