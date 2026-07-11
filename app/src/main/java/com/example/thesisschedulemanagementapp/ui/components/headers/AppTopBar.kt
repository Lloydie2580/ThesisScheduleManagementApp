package com.example.thesisschedulemanagementapp.ui.components.headers

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    onActionClick: (() -> Unit)? = null
) {

    CenterAlignedTopAppBar(

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },

        navigationIcon = {

            if (navigationIcon != null && onNavigationClick != null) {

                IconButton(
                    onClick = onNavigationClick
                ) {

                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = null
                    )

                }

            }

        },

        actions = {

            if (actionIcon != null && onActionClick != null) {

                IconButton(
                    onClick = onActionClick
                ) {

                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null
                    )

                }

            }

        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

    )

}