package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import com.dj.insulink.feature.ui.components.DateTimeInput
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.core.ui.theme.InsulinkTheme
import com.dj.insulink.feature.domain.models.Ingredient
import com.dj.insulink.feature.domain.models.MealIngredient
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog(
    mealName: StateFlow<String>,
    onMealNameChange: (String) -> Unit,
    mealComment: StateFlow<String>,
    onMealCommentChange: (String) -> Unit,
    mealDate: StateFlow<Long>,
    onMealDateChange: (Long) -> Unit,
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
    val mealNameValue by mealName.collectAsState()
    val mealCommentValue by mealComment.collectAsState()
    val mealDateValue by mealDate.collectAsState()
    val searchQueryValue by searchQuery
    val searchResultsValue by searchResults
    val selectedIngredientsValue by selectedIngredients
    val isLoadingValue by isLoading

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.commonPadding16)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Meal",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding16))

                OutlinedTextField(
                    value = mealNameValue,
                    onValueChange = onMealNameChange,
                    label = { Text("Meal Name") },
                    placeholder = { Text("Enter meal name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding16))

                DateTimeInput(
                    selectedTimestamp = mealDateValue,
                    onTimestampSelected = onMealDateChange
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding16))

                OutlinedTextField(
                    value = searchQueryValue,
                    onValueChange = onSearchQueryChange,
                    label = { Text("Add Ingredients") },
                    placeholder = { Text("Search ingredients...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = onCreateIngredient) {
                                Icon(Icons.Default.Add, contentDescription = "Create ingredient")
                            }
                            IconButton(onClick = onShowMyIngredients) {
                                Icon(Icons.Default.Person, contentDescription = "My ingredients")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding8))

                if (searchResultsValue.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            ),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(searchResultsValue) { ingredient ->
                            IngredientSearchItem(
                                ingredient = ingredient,
                                onAdd = { onAddIngredient(ingredient, 100.0) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding16))

                Text(
                    text = "Added Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding8))

                if (selectedIngredientsValue.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add ingredients",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "No ingredients added yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Search and add ingredients above",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            ),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(selectedIngredientsValue) { mealIngredient ->
                            AddedIngredientItem(
                                mealIngredient = mealIngredient,
                                onRemove = { onRemoveIngredient(mealIngredient) },
                                onQuantityChange = { quantity -> onUpdateIngredientQuantity(mealIngredient, quantity) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding16))

                Text(
                    text = "Nutrition Facts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding8))

                val totalCalories = selectedIngredientsValue.sumOf { (it.ingredient.caloriesPer100g * it.quantity / 100).toInt() }
                val totalProtein = selectedIngredientsValue.sumOf { it.ingredient.proteinPer100g * it.quantity / 100 }
                val totalFat = selectedIngredientsValue.sumOf { it.ingredient.fatPer100g * it.quantity / 100 }
                val totalCarbs = selectedIngredientsValue.sumOf { it.ingredient.carbsPer100g * it.quantity / 100 }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NutritionCard("Calories", totalCalories.toString(), InsulinkTheme.colors.insulinkBlue)
                    NutritionCard("Protein", "${String.format("%.1f", totalProtein)}g", InsulinkTheme.colors.glucoseNormal)
                    NutritionCard("Fats", "${String.format("%.1f", totalFat)}g", InsulinkTheme.colors.lastDropLabel)
                    NutritionCard("Carb", "${String.format("%.1f", totalCarbs)}g", InsulinkTheme.colors.glucoseLow)
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.commonPadding16))

                OutlinedTextField(
                    value = mealCommentValue,
                    onValueChange = onMealCommentChange,
                    label = { Text("Comment (Optional)") },
                    placeholder = { Text("Add a comment about this meal") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing16))

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoadingValue && mealNameValue.isNotEmpty() && selectedIngredientsValue.isNotEmpty()
                ) {
                    if (isLoadingValue) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save Meal")
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
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.commonPadding12),
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
                    text = "${ingredient.caloriesPer100g.toInt()} cal per 100g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add ingredient")
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
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.commonPadding12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mealIngredient.ingredient.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${(mealIngredient.ingredient.caloriesPer100g * mealIngredient.quantity / 100).toInt()} cal per ${mealIngredient.quantity.toInt()}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Quantity (g)",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { newValue ->
                        quantityText = newValue
                        newValue.toDoubleOrNull()?.let { onQuantityChange(it) }
                    },
                    modifier = Modifier.width(80.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, contentDescription = "Remove ingredient")
                }
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
            .width(70.dp)
            .height(60.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(8.dp)
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