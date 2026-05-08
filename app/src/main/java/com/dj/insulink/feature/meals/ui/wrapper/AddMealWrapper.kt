package com.dj.insulink.feature.meals.ui.wrapper

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.feature.meals.ui.AddMealScreen
import com.dj.insulink.feature.meals.ui.AddMealScreenParams
import com.dj.insulink.feature.meals.ui.viewmodel.MealsViewModel

@Composable
fun AddMealWrapper(
    currentUser: User?,
    navigateBack: () -> Unit
) {
    val viewModel: MealsViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

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

    LaunchedEffect(searchQuery.value) {
        viewModel.searchIngredients(searchQuery.value)
    }

    AddMealScreen(
        params = AddMealScreenParams(
            mealName = newMealName,
            onMealNameChange = viewModel::setNewMealName,
            mealComment = newMealComment,
            onMealCommentChange = viewModel::setNewMealComment,
            mealTimestamp = newMealTimestamp,
            onMealTimestampChange = viewModel::setNewMealTimestamp,
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
            onSave = {
                viewModel.submitNewMeal(currentUser?.uid) {
                    navigateBack()
                }
            },
            onNavigateBack = navigateBack,
            createCustomIngredient = {
                viewModel.createCustomIngredient(currentUser?.uid, it)
            },
            deleteCustomIngredient = viewModel::deleteCustomIngredient
        )
    )
}
