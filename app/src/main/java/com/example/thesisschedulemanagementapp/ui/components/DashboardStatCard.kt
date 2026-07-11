package com.example.thesisschedulemanagementapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.thesisschedulemanagementapp.ui.theme.Dimens
import com.example.thesisschedulemanagementapp.ui.theme.Elevation


@Composable
fun DashboardStatCard(

    title: String,

    value: String,

    icon: ImageVector,

    modifier: Modifier = Modifier

) {


    Card(

        modifier = modifier,

        shape = MaterialTheme.shapes.large,

        elevation = CardDefaults.cardElevation(
            Elevation.Card
        )

    ) {


        Column(

            modifier = Modifier.padding(
                Dimens.SpaceL
            ),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.spacedBy(
                Dimens.SpaceS
            )

        ) {


            Icon(

                imageVector = icon,

                contentDescription = null

            )


            Text(

                text = value,

                style = MaterialTheme.typography.headlineMedium

            )


            Text(

                text = title,

                style = MaterialTheme.typography.bodyMedium

            )

        }

    }

}