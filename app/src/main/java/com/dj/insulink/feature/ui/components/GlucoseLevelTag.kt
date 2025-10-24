package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.core.ui.theme.InsulinkTheme

@Composable
fun GlucoseLevelTag(
    glucoseLevel: Int
) {
    val (text, backgroundColor, textColor) = when {
        glucoseLevel < 70 -> Triple(
            "LOW",
            InsulinkTheme.colors.glucoseLow.copy(alpha = 0.2f),
            InsulinkTheme.colors.glucoseLow
        )
        glucoseLevel <= 180 -> Triple(
            "NORMAL",
            InsulinkTheme.colors.glucoseNormal.copy(alpha = 0.2f),
            InsulinkTheme.colors.glucoseNormal
        )
        else -> Triple(
            "HIGH",
            InsulinkTheme.colors.glucoseHigh.copy(alpha = 0.2f),
            InsulinkTheme.colors.glucoseHigh
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12))
            .background(backgroundColor)
            .padding(
                horizontal = InsulinkTheme.dimens.commonPadding8,
                vertical = InsulinkTheme.dimens.commonPadding4
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}