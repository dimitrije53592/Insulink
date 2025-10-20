package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.core.ui.theme.dimens

@Composable
public fun NutritionItem(
    label: String,
    value: String,
    unit: String,
    color: Color, // Using hardcoded color for brand appearance
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius8),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
            .fillMaxWidth(0.5f) // Half width for two columns
            .padding(horizontal = MaterialTheme.dimens.commonSpacing4)
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimens.commonPadding12)
        ) {
            Text(
                text = value,
                // Text color should contrast with the hardcoded background color
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}