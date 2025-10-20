package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.Meal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MealItem(meal: Meal, onSwipeFromStartToEnd: () -> Unit) {
    // Determine if any key nutritional/tracking data is present to show the full details vs. placeholder
    val hasContent = meal.calories != null || meal.carbs != null || meal.protein != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // This enables the swipe to delete action - commented out as the function is not defined here
            // .swipeToDismiss(onDismissed = { onSwipeFromStartToEnd() })
            .padding(horizontal = MaterialTheme.dimens.commonPadding12),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.commonElevation2),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Theme Color
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.commonPadding16)
        ) {
            // Left Content: Meal Name and Time
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(meal.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Right Content: Details or Placeholder Circle (as requested by the user's snippet logic)
            if (hasContent) {
                Column(modifier = Modifier.align(Alignment.CenterEnd), horizontalAlignment = Alignment.End) {
                    // Calories
                    if (meal.calories != null) {
                        Text(
                            text = "${meal.calories} cal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing4))
                    }

                    // Protein Info
                    if (meal.protein != null) {
                        Text(
                            text = "Protein: ${String.format("%.1f", meal.protein)}g",
                            color = Color(0xFF4A7BF6), // Primary Blue
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Carbs Info
                    if (meal.carbs != null) {
                        Text(
                            text = "Carbs: ${String.format("%.1f", meal.carbs)}g",
                            color = Color(0xFF6F9940), // Darker Green
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Placeholder Blue Circle (similar to Morning Snack card in the image)
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.commonSpacing16)
                        .background(Color(0xFFE6F3FF), shape = RoundedCornerShape(8.dp)) // Light Blue
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}
