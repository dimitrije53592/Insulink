package com.dj.insulink.feature.meals.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.InsulinkTheme
import com.dj.insulink.feature.meals.domain.model.Ingredient
import com.dj.insulink.feature.meals.domain.model.MealIngredient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog(
    mealName: State<String>,
    onMealNameChange: (String) -> Unit,
    mealComment: State<String>,
    onMealCommentChange: (String) -> Unit,
    mealTimestamp: State<Long>,
    onMealTimestampChange: (Long) -> Unit,
    searchQuery: State<String>,
    onSearchQueryChange: (String) -> Unit,
    searchResults: State<List<Ingredient>>,
    selectedIngredients: State<List<MealIngredient>>,
    onAddIngredient: (Ingredient, Double) -> Unit,
    onRemoveIngredient: (MealIngredient) -> Unit,
    onUpdateIngredientQuantity: (MealIngredient, Double) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    isLoading: State<Boolean>,
    onCreateIngredient: () -> Unit,
    onShowMyIngredients: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(DIALOG_HEIGHT_FRACTION),
            shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(InsulinkTheme.dimens.commonPadding16)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.meals_screen_add_meal_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "")
                    }
                }

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                OutlinedTextField(
                    value = mealName.value,
                    onValueChange = onMealNameChange,
                    label = { Text(stringResource(R.string.meals_screen_meal_name_label)) },
                    placeholder = { Text(stringResource(R.string.meals_screen_meal_name_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                DateTimeInput(
                    selectedTimestamp = mealTimestamp.value,
                    onTimestampSelected = onMealTimestampChange
                )

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = onSearchQueryChange,
                    label = { Text(stringResource(R.string.meals_screen_add_ingredients_label)) },
                    placeholder = { Text(stringResource(R.string.meals_screen_search_ingredients_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = onCreateIngredient) {
                                Icon(Icons.Default.Add, contentDescription = "")
                            }
                            IconButton(onClick = onShowMyIngredients) {
                                Icon(Icons.Default.Person, contentDescription = "")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding8))

                if (searchResults.value.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(InsulinkTheme.dimens.searchResultsListHeight)
                            .border(
                                InsulinkTheme.dimens.commonButtonBorder1,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius8)
                            ),
                        contentPadding = PaddingValues(InsulinkTheme.dimens.commonPadding8)
                    ) {
                        items(searchResults.value) { ingredient ->
                            IngredientSearchItem(
                                ingredient = ingredient,
                                onAdd = { onAddIngredient(ingredient, 100.0) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                Text(
                    text = stringResource(R.string.meals_screen_added_ingredients_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding8))

                if (selectedIngredients.value.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(InsulinkTheme.dimens.searchResultsListHeight)
                            .border(
                                InsulinkTheme.dimens.commonButtonBorder1,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius8)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "",
                                modifier = Modifier.size(InsulinkTheme.dimens.sideDrawerIconSize),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(R.string.meals_screen_no_ingredients_title),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(R.string.meals_screen_no_ingredients_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(InsulinkTheme.dimens.ingredientsListHeight)
                            .border(
                                InsulinkTheme.dimens.commonButtonBorder1,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius8)
                            ),
                        contentPadding = PaddingValues(InsulinkTheme.dimens.commonPadding8)
                    ) {
                        items(selectedIngredients.value) { mealIngredient ->
                            AddedIngredientItem(
                                mealIngredient = mealIngredient,
                                onRemove = { onRemoveIngredient(mealIngredient) },
                                onQuantityChange = { quantity ->
                                    onUpdateIngredientQuantity(mealIngredient, quantity)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                Text(
                    text = stringResource(R.string.meals_screen_nutrition_facts_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding8))

                val totalCalories =
                    selectedIngredients.value.sumOf { (it.ingredient.caloriesPer100g * it.quantity / 100).toInt() }
                val totalProtein =
                    selectedIngredients.value.sumOf { it.ingredient.proteinPer100g * it.quantity / 100 }
                val totalFat =
                    selectedIngredients.value.sumOf { it.ingredient.fatPer100g * it.quantity / 100 }
                val totalCarbs =
                    selectedIngredients.value.sumOf { it.ingredient.carbsPer100g * it.quantity / 100 }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NutritionCard(stringResource(R.string.meals_screen_calories_label), totalCalories.toString(), InsulinkTheme.colors.insulinkBlue)
                    NutritionCard(stringResource(R.string.meals_screen_protein_label), stringResource(R.string.meals_screen_grams_value, String.format("%.1f", totalProtein)), InsulinkTheme.colors.glucoseNormal)
                    NutritionCard(stringResource(R.string.meals_screen_fats_label), stringResource(R.string.meals_screen_grams_value, String.format("%.1f", totalFat)), InsulinkTheme.colors.lastDropLabel)
                    NutritionCard(stringResource(R.string.meals_screen_carb_label), stringResource(R.string.meals_screen_grams_value, String.format("%.1f", totalCarbs)), InsulinkTheme.colors.glucoseLow)
                }

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding16))

                OutlinedTextField(
                    value = mealComment.value,
                    onValueChange = onMealCommentChange,
                    label = { Text(stringResource(R.string.meals_screen_comment_label)) },
                    placeholder = { Text(stringResource(R.string.meals_screen_comment_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing16))

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(InsulinkTheme.dimens.commonButtonHeight50),
                    enabled = !isLoading.value && mealName.value.isNotEmpty() && selectedIngredients.value.isNotEmpty()
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(InsulinkTheme.dimens.commonProgressIndicatorSize20),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.meals_screen_save_meal))
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientSearchItem(
    ingredient: Ingredient,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAdd() }
            .padding(vertical = InsulinkTheme.dimens.commonPadding4),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(InsulinkTheme.dimens.commonPadding12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.meals_screen_cal_per_100g, ingredient.caloriesPer100g.toInt()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    }
}

@Composable
private fun AddedIngredientItem(
    mealIngredient: MealIngredient,
    onRemove: () -> Unit,
    onQuantityChange: (Double) -> Unit
) {
    var quantityText by remember { mutableStateOf(mealIngredient.quantity.toString()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = InsulinkTheme.dimens.commonPadding4),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(InsulinkTheme.dimens.commonPadding12),
            ) {
                Text(
                    text = mealIngredient.ingredient.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.meals_screen_cal_per_grams, (mealIngredient.ingredient.caloriesPer100g * mealIngredient.quantity / 100).toInt(), mealIngredient.quantity.toInt()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(InsulinkTheme.dimens.commonSpacing8))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.meals_screen_quantity_label),
                        style = MaterialTheme.typography.bodySmall
                    )
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { newValue ->
                            quantityText = newValue
                            newValue.toDoubleOrNull()?.let { onQuantityChange(it) }
                        },
                        modifier = Modifier.width(InsulinkTheme.dimens.quantityFieldWidth),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Close, contentDescription = "")
            }
        }
    }
}

@Composable
private fun NutritionCard(
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(InsulinkTheme.dimens.nutritionCardWidth)
            .height(InsulinkTheme.dimens.nutritionCardHeight),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius8)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private const val DIALOG_HEIGHT_FRACTION = 0.9f
