package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.GlucoseReading
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GlucoseReadingItem(
    glucoseReading: GlucoseReading,
    onSwipeFromStartToEnd: () -> Unit
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd) {
                onSwipeFromStartToEnd()
                true
            } else {
                false
            }
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromEndToStart = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.commonPadding12),
        backgroundContent = {}
    ) {
        GlucoseReadingItemContent(glucoseReading = glucoseReading)
    }
}

@Composable
private fun GlucoseReadingItemContent(
    glucoseReading: GlucoseReading
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.border(
            width = MaterialTheme.dimens.commonButtonBorder1,
            color = Color.LightGray,
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
        )
    ) {
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
        Column {
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
            Row {
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
            Text(glucoseReading.comment)
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
        }
        Spacer(Modifier.weight(1f))
        Row {
            Text(
                text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(glucoseReading.timestamp))
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(Date(glucoseReading.timestamp)),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
    }
    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))

}