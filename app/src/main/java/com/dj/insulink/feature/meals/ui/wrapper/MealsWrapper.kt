package com.dj.insulink.feature.meals.ui.wrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.feature.meals.ui.MealsScreen
import com.dj.insulink.feature.meals.ui.MealsScreenParams
import com.dj.insulink.feature.meals.ui.viewmodel.MealsViewModel

@Composable
fun MealsWrapper(
    currentUser: User?
) {
    val viewModel: MealsViewModel = hiltViewModel()

    val mealsForSelectedDate = viewModel.mealsForSelectedDate.collectAsStateWithLifecycle()
    val dailyNutrition = viewModel.dailyNutrition.collectAsStateWithLifecycle()
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle()
    val showAddMealDialog = viewModel.showAddMealDialog.collectAsStateWithLifecycle()
    val newMealName = viewModel.newMealName.collectAsStateWithLifecycle()
    val newMealComment = viewModel.newMealComment.collectAsStateWithLifecycle()
    val newMealTimestamp = viewModel.newMealTimestamp.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsStateWithLifecycle()
    val selectedIngredients = viewModel.selectedIngredients.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val showCreateIngredientDialog = viewModel.showCreateIngredientDialog.collectAsStateWithLifecycle()
    val showMyIngredientsDialog = viewModel.showMyIngredientsDialog.collectAsStateWithLifecycle()
    val userIngredients = viewModel.userIngredients.collectAsStateWithLifecycle()

    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            viewModel.fetchAllMealsForUserAndUpdateDatabase(it)
            viewModel.loadDailyNutritionForDate(viewModel.selectedDate.value)
        }
    }

    LaunchedEffect(searchQuery.value) {
        viewModel.searchIngredients(searchQuery.value)
    }

    MealsScreen(
        params = MealsScreenParams(
            mealsForSelectedDate = mealsForSelectedDate,
            dailyNutrition = dailyNutrition,
            selectedDate = selectedDate,
            setSelectedDate = viewModel::setSelectedDate,
            showAddMealDialog = showAddMealDialog,
            setShowAddMealDialog = viewModel::setShowAddMealDialog,
            newMealName = newMealName,
            setNewMealName = viewModel::setNewMealName,
            newMealComment = newMealComment,
            setNewMealComment = viewModel::setNewMealComment,
            newMealTimestamp = newMealTimestamp,
            setNewMealTimestamp = viewModel::setNewMealTimestamp,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery.value = it },
            searchResults = searchResults,
            selectedIngredients = selectedIngredients,
            onAddIngredient = viewModel::addIngredient,
            onRemoveIngredient = viewModel::removeIngredient,
            onUpdateIngredientQuantity = viewModel::updateIngredientQuantity,
            isLoading = isLoading,
            showCreateIngredientDialog = showCreateIngredientDialog,
            setShowCreateIngredientDialog = viewModel::setShowCreateIngredientDialog,
            showMyIngredientsDialog = showMyIngredientsDialog,
            setShowMyIngredientsDialog = viewModel::setShowMyIngredientsDialog,
            userIngredients = userIngredients,
            submitNewMeal = {
                viewModel.submitNewMeal(currentUser?.uid)
            },
            deleteMeal = {
                viewModel.deleteMeal(currentUser?.uid, it)
            },
            createCustomIngredient = {
                viewModel.createCustomIngredient(currentUser?.uid, it)
            },
            deleteCustomIngredient = viewModel::deleteCustomIngredient
        )
    )
}
