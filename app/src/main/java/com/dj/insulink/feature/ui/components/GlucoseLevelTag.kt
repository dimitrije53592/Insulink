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
import com.dj.insulink.core.ui.theme.dimens

@Composable
fun GlucoseLevelTag(
    glucoseLevel: Int
) {
    val (text, backgroundColor, textColor) = when {
        glucoseLevel < 70 -> Triple("LOW", Color(0xFFFFEBEE), Color(0xFFD32F2F))
        glucoseLevel <= 180 -> Triple("NORMAL", Color(0xFFE8F5E9), Color(0xFF388E3C))
        else -> Triple("HIGH", Color(0xFFFFF9C4), Color(0xFFF57F17))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12))
            .background(backgroundColor)
            .padding(
                horizontal = MaterialTheme.dimens.commonPadding8,
                vertical = MaterialTheme.dimens.commonPadding4
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