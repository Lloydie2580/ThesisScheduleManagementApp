package com.example.thesisschedulemanagementapp.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Composable
fun GroupCard(
    group: StudentGroup
) {

    AppCard {

        Column(

            verticalArrangement =
                Arrangement.spacedBy(
                    Dimens.SpaceS
                )

        ) {

            Text(
                text = group.groupCode,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = group.researchTitle
            )

            Text(
                "Adviser: ${group.adviserName ?: "Assigned adviser"}"
            )

            if (group.members.isNotEmpty()) {

                Text(
                    "Members: ${
                        group.members.joinToString {
                            it.fullName
                        }
                    }"
                )

            }

        }

    }

}