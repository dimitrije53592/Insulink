package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dj.insulink.core.ui.theme.InsulinkTheme
import com.dj.insulink.feature.domain.models.Ingredient
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MyIngredientsDialog(
    userIngredients: StateFlow<List<Ingredient>>,
    onDismiss: () -> Unit,
    onCreateIngredient: () -> Unit,
    onDeleteIngredient: (Ingredient) -> Unit,
    isLoading: Boolean = false
) {
    val ingredients by userIngredients.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(InsulinkTheme.dimens.commonPadding16)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Ingredients",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row {
                        IconButton(onClick = onCreateIngredient) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Create ingredient",
                                tint = InsulinkTheme.colors.insulinkBlue
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                if (ingredients.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "No ingredients",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))
                            Text(
                                text = "No custom ingredients yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Create your first custom ingredient",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))
                            Button(
                                onClick = onCreateIngredient,
                                modifier = Modifier
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                InsulinkTheme.colors.insulinkBlue,
                                                InsulinkTheme.colors.insulinkPurple
                                            )
                                        ),
                                        shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12)
                                    ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12)
                            ) {
                                Text("Create Ingredient", color = Color.White)
                            }
                        }
                    }
                } else {
                    // Ingredients list
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ingredients) { ingredient ->
                            IngredientItem(
                                ingredient = ingredient,
                                onDelete = { onDeleteIngredient(ingredient) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientItem(
    ingredient: Ingredient,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(InsulinkTheme.dimens.commonPadding16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${ingredient.caloriesPer100g.toInt()} cal per 100g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    text = "P: ${String.format("%.1f", ingredient.proteinPer100g)}g | " +
                            "C: ${String.format("%.1f", ingredient.carbsPer100g)}g | " +
                            "F: ${String.format("%.1f", ingredient.fatPer100g)}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete ingredient",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}