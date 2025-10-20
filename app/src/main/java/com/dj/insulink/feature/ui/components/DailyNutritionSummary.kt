package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.DailyNutrition

@Composable
fun DailyNutritionSummary(nutrition: DailyNutrition) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.commonPadding12)
    ) {
        Text(
            text = "Daily Nutrition",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = MaterialTheme.dimens.commonSpacing12)
        )

        // Row 1: Calories and Carbs
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.commonSpacing8)
        ) {
            NutritionItem(
                label = "Calories",
                value = "${nutrition.calories}",
                unit = "",
                color = Color(0xFFE6F3FF), // Light Blue
                modifier = Modifier.weight(1f)
            )
            NutritionItem(
                label = "Carbs",
                value = "${nutrition.carbs}g",
                unit = "",
                color = Color(0xFFE8FCE4), // Light Green
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))

        // Row 2: Protein and Fat
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.commonSpacing8)
        ) {
            NutritionItem(
                label = "Protein",
                value = "${nutrition.protein}g",
                unit = "",
                color = Color(0xFFFDE4FF), // Light Purple
                modifier = Modifier.weight(1f)
            )
            NutritionItem(
                label = "Fat",
                value = "${nutrition.fat}g",
                unit = "",
                color = Color(0xFFFFF1E6), // Light Orange
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))

        // Row 3: Sugar and Salt
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.commonSpacing8)
        ) {
            NutritionItem(
                label = "Sugar",
                value = "${nutrition.sugar}g",
                unit = "",
                color = Color(0xFFFFEAEA), // Light Red
                modifier = Modifier.weight(1f)
            )
            NutritionItem(
                label = "Salt",
                value = "${nutrition.salt}g",
                unit = "",
                color = Color(0xFFEEEEEE), // Light Gray
                modifier = Modifier.weight(1f)
            )
        }
    }
}
