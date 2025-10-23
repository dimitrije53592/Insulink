package com.dj.insulink.feature.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.core.ui.theme.InsulinkTheme
import com.dj.insulink.feature.ui.components.AddMealDialog
import com.dj.insulink.feature.ui.components.CreateIngredientDialog
import com.dj.insulink.feature.ui.components.DailyNutritionSummary
import com.dj.insulink.feature.ui.components.MealItem
import com.dj.insulink.feature.ui.components.MyIngredientsDialog
import com.dj.insulink.feature.ui.viewmodel.MealsViewModel
import com.dj.insulink.feature.domain.models.DailyNutrition
import com.dj.insulink.feature.domain.models.Meal

@Composable
fun MealsScreen(
    params: MealsScreenParams? = null,
    currentUserId: String? = null
) {
    val viewModel: MealsViewModel = hiltViewModel()

    val allMeals = if (params != null) params.allMeals else viewModel.mealsForSelectedDate.collectAsState()
    val dailyNutrition = if (params != null) params.dailyNutritionData else viewModel.dailyNutrition.collectAsState()
    val showAddMealDialog = if (params != null) params.showAddMealDialog else viewModel.showAddMealDialog.collectAsState()
    val newMealName = if (params != null) params.newMealName else viewModel.newMealName
    val newMealComment = if (params != null) params.newMealComment else viewModel.newMealComment
    val newMealTimestamp = if (params != null) params.newMealTimestamp else viewModel.newMealTimestamp.collectAsState()
    val searchQuery = remember { mutableStateOf("") }
    val searchResults = viewModel.searchResults.collectAsState()
    val selectedIngredients = viewModel.selectedIngredients.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val selectedDate = viewModel.selectedDate.collectAsState()
    val showCreateIngredientDialog = viewModel.showCreateIngredientDialog.collectAsState()
    val showMyIngredientsDialog = viewModel.showMyIngredientsDialog.collectAsState()
    val userIngredients = viewModel.userIngredients.collectAsState()

    LaunchedEffect(currentUserId) {
        if (currentUserId != null) {
            viewModel.setCurrentUserEmail(currentUserId)
        } else {
            viewModel.setCurrentUserEmail("dummy@example.com")
        }
    }

    LaunchedEffect(selectedDate.value) {
        viewModel.loadMealsForSelectedDate()
        viewModel.loadDailyNutritionForDate(selectedDate.value)
    }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
                DatePickerCard(
                    selectedDate = selectedDate.value,
                    onDateSelected = { date ->
                        viewModel.setSelectedDate(date)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.commonPadding12)
                )
            }

            item {
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
                Card(
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.commonElevation2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.commonPadding12)
                ) {
                    DailyNutritionSummary(dailyNutrition.value)
                }
            }

            item {
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
                Text(
                    text = "Meals",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(
                        start = MaterialTheme.dimens.commonPadding16,
                        top = MaterialTheme.dimens.commonPadding8,
                        bottom = MaterialTheme.dimens.commonPadding8
                    )
                )
            }

            items(items = allMeals.value, key = { it.id }) { meal ->
                MealItem(
                    meal = meal,
                    onSwipeFromStartToEnd = {
                        if (params != null) {
                            params.deleteMeal(meal)
                        } else {
                            viewModel.deleteMeal(meal)
                        }
                    }
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonPadding8))
            }

            item {
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing64))
            }
        }

        FloatingActionButton(
            onClick = {
                if (params != null) {
                    params.setNewMealTimestamp(System.currentTimeMillis())
                    params.setShowAddMealDialog(true)
                } else {
                    viewModel.setNewMealTimestamp(System.currentTimeMillis())
                    viewModel.setShowAddMealDialog(true)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.dimens.commonPadding16),
            containerColor = InsulinkTheme.colors.insulinkBlue
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "Add Meal"
            )
        }
    }

    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            viewModel.searchIngredients(searchQuery.value)
        }
    }

    if (showAddMealDialog.value) {
        AddMealDialog(
            mealName = viewModel.newMealName,
            onMealNameChange = viewModel::setNewMealName,
            mealComment = viewModel.newMealComment,
            onMealCommentChange = viewModel::setNewMealComment,
            mealDate = viewModel.newMealTimestamp,
            onMealDateChange = viewModel::setNewMealTimestamp,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery.value = it },
            searchResults = searchResults,
            selectedIngredients = selectedIngredients,
            onAddIngredient = viewModel::addIngredient,
            onRemoveIngredient = viewModel::removeIngredient,
            onUpdateIngredientQuantity = viewModel::updateIngredientQuantity,
            onDismiss = {
                if (params != null) {
                    params.setShowAddMealDialog(false)
                } else {
                    viewModel.setShowAddMealDialog(false)
                }
            },
            onSave = {
                if (params != null) {
                    params.submitNewMeal()
                } else {
                    viewModel.submitNewMeal()
                }
            },
            isLoading = isLoading,
            onCreateIngredient = { viewModel.setShowCreateIngredientDialog(true) },
            onShowMyIngredients = { viewModel.setShowMyIngredientsDialog(true) }
        )
    }

    if (showCreateIngredientDialog.value) {
        CreateIngredientDialog(
            onDismiss = { viewModel.setShowCreateIngredientDialog(false) },
            onSave = { ingredient ->
                viewModel.createCustomIngredient(ingredient)
            },
            isLoading = isLoading.value
        )
    }

    if (showMyIngredientsDialog.value) {
        MyIngredientsDialog(
            userIngredients = viewModel.userIngredients,
            onDismiss = { viewModel.setShowMyIngredientsDialog(false) },
            onCreateIngredient = { viewModel.setShowCreateIngredientDialog(true) },
            onDeleteIngredient = { ingredient ->
                viewModel.deleteCustomIngredient(ingredient)
            },
            isLoading = isLoading.value
        )
    }
}

data class MealsScreenParams(
    val dailyNutritionData: State<DailyNutrition>,
    val allMeals: State<List<Meal>>,
    val newMealTimestamp: State<Long>,
    val setNewMealTimestamp: (Long) -> Unit,
    val newMealName: State<String>,
    val setNewMealName: (String) -> Unit,
    val newMealCalories: State<String>,
    val setNewMealCalories: (String) -> Unit,
    val newMealCarbs: State<String>,
    val setNewMealCarbs: (String) -> Unit,
    val newMealProtein: State<String>,
    val setNewMealProtein: (String) -> Unit,
    val newMealFat: State<String>,
    val setNewMealFat: (String) -> Unit,
    val newMealSugar: State<String>,
    val setNewMealSugar: (String) -> Unit,
    val newMealSalt: State<String>,
    val setNewMealSalt: (String) -> Unit,
    val newMealComment: State<String>,
    val setNewMealComment: (String) -> Unit,
    val showAddMealDialog: State<Boolean>,
    val setShowAddMealDialog: (Boolean) -> Unit,
    val submitNewMeal: () -> Unit,
    val deleteMeal: (Meal) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerCard(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    val isToday = remember(selectedDate) {
        val today = Calendar.getInstance()
        val selected = Calendar.getInstance().apply { timeInMillis = selectedDate }
        today.get(Calendar.YEAR) == selected.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == selected.get(Calendar.DAY_OF_YEAR)
    }

    Card(
        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.commonElevation2),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker.value = true
                }
                .padding(vertical = MaterialTheme.dimens.commonPadding8)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = if (isToday) "Today" else dateFormatter.format(Date(selectedDate)),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = dayFormatter.format(Date(selectedDate)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { dateMillis ->
                            onDateSelected(dateMillis)
                        }
                        showDatePicker.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker.value = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}