package com.dj.insulink.feature.meals.ui.wrapper

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.feature.meals.ui.MealsScreen
import com.dj.insulink.feature.meals.ui.MealsScreenParams
import com.dj.insulink.feature.meals.ui.viewmodel.MealsViewModel

@Composable
fun MealsWrapper(
    currentUser: User?,
    navigateToAddMeal: () -> Unit
) {
    val viewModel: MealsViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

    val mealsForSelectedDate = viewModel.mealsForSelectedDate.collectAsStateWithLifecycle()
    val dailyNutrition = viewModel.dailyNutrition.collectAsStateWithLifecycle()
    val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle()

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            viewModel.fetchAllMealsForUserAndUpdateDatabase(it)
            viewModel.loadDailyNutritionForDate(viewModel.selectedDate.value)
        }
    }

    MealsScreen(
        params = MealsScreenParams(
            mealsForSelectedDate = mealsForSelectedDate,
            dailyNutrition = dailyNutrition,
            selectedDate = selectedDate,
            setSelectedDate = viewModel::setSelectedDate,
            deleteMeal = {
                viewModel.deleteMeal(currentUser?.uid, it)
            },
            onAddMealClick = {
                viewModel.prepareNewMeal()
                navigateToAddMeal()
            }
        )
    )
}
