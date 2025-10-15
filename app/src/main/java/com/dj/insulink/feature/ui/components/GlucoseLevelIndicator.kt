package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.core.ui.theme.dimens

@Composable
fun GlucoseLevelIndicator(
    glucoseLevel: Int?,
    modifier: Modifier = Modifier
) {
    if(glucoseLevel != null) {
        val (text, iconColor) = when {
            glucoseLevel < 70 -> "Below target range" to Color(0xFFEF5350) // Red
            glucoseLevel <= 180 -> "In target range" to Color(0xFF66BB6A) // Green
            else -> "Above target range" to Color(0xFFFFEE58) // Yellow
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Circle,
                tint = iconColor,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.commonSpacing24)
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}