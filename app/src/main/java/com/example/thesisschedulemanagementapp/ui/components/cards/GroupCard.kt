package com.example.thesisschedulemanagementapp.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thesisschedulemanagementapp.data.model.StudentGroup
import com.example.thesisschedulemanagementapp.ui.components.common.AppCard
import com.example.thesisschedulemanagementapp.ui.theme.*

private val ChipPalette = listOf(
    ChipLavender to ChipLavenderOn,
    ChipMint to ChipMintOn,
    ChipPeach to ChipPeachOn,
    ChipSky to ChipSkyOn
)

@Composable
fun GroupCard(group: StudentGroup) {

    val (bg, on) = ChipPalette[kotlin.math.abs(group.groupCode.hashCode()) % ChipPalette.size]

    AppCard {
        Row(verticalAlignment = Alignment.Top) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.groupCode.take(2).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = on
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceM))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXS)
            ) {
                Text(
                    text = group.groupCode,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = group.researchTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceXS))

                Text(
                    text = "Adviser: ${group.adviserName ?: "Unassigned"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                if (group.members.isNotEmpty()) {
                    Text(
                        text = group.members.joinToString { it.fullName },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}