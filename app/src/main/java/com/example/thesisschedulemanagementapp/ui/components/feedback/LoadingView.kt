package com.example.thesisschedulemanagementapp.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Composable
fun LoadingView(
    message: String = "Loading..."
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
    ) {

        CircularProgressIndicator()

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )

    }

}