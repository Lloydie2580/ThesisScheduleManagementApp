package com.example.thesisschedulemanagementapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thesisschedulemanagementapp.ui.theme.ThesisScheduleManagementTheme
import com.example.thesisschedulemanagementapp.ui.theme.Dimens

@Preview(showBackground = true)
@Composable
fun ButtonShowcasePreview() {

    ThesisScheduleManagementTheme {

        Surface {

            Column(

                modifier = Modifier.padding(Dimens.SpaceL),

                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)

            ) {

                AppButton(
                    text = "Save Schedule",
                    icon = Icons.Default.Save,
                    onClick = {}
                )

                AppButton(
                    text = "Secondary",
                    buttonType = ButtonType.SECONDARY,
                    onClick = {}
                )

                AppButton(
                    text = "Outlined",
                    buttonType = ButtonType.OUTLINED,
                    onClick = {}
                )

                AppButton(
                    text = "Delete",
                    buttonType = ButtonType.DANGER,
                    icon = Icons.Default.Delete,
                    onClick = {}
                )

                AppButton(
                    text = "Loading...",
                    loading = true,
                    onClick = {}
                )

            }

        }

    }

}