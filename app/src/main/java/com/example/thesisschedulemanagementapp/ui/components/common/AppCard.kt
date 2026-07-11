package com.example.thesisschedulemanagementapp.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.Elevation

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            Elevation.Card
        )
    ) {

        Column(
            modifier = Modifier.padding(Dimens.SpaceL)
        ) {

            content()

        }

    }

}