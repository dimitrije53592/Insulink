package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.GlucoseReading

@Composable
fun FriendsListItem(
    friend: String,
    glucoseReading: GlucoseReading
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = MaterialTheme.dimens.commonButtonBorder1,
                color = Color.LightGray,
                shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
            )
            .padding(vertical = MaterialTheme.dimens.commonPadding12)
    ) {
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
        Column {
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
            Row {
                Text(
                    text = friend,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
            }
            Text("Last reading: ...")
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
        }
        Spacer(Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(
                    R.string.glucose_screen_value_display_label,
                    glucoseReading.value
                ),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
            GlucoseLevelTag(glucoseLevel = glucoseReading.value)
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
    }
    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
}