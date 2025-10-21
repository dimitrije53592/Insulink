package com.dj.insulink.feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.feature.data.repository.MealRepository
import com.dj.insulink.feature.data.room.InitialData
import com.dj.insulink.feature.domain.models.DailyNutrition
import com.dj.insulink.feature.domain.models.Ingredient
import com.dj.insulink.feature.domain.models.Meal
import com.dj.insulink.feature.domain.models.MealIngredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val initialData: InitialData
) : ViewModel() {

    private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())
    val allMeals: StateFlow<List<Meal>> = _allMeals.asStateFlow()

    private val _dailyNutrition = MutableStateFlow(DailyNutrition(0, 0, 0, 0, 0, 0.0))
    val dailyNutrition: StateFlow<DailyNutrition> = _dailyNutrition.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Ingredient>>(emptyList())
    val searchResults: StateFlow<List<Ingredient>> = _searchResults.asStateFlow()

    private val _selectedIngredients = MutableStateFlow<List<MealIngredient>>(emptyList())
    val selectedIngredients: StateFlow<List<MealIngredient>> = _selectedIngredients.asStateFlow()

    // New meal form state
    private val _newMealName = MutableStateFlow("")
    val newMealName: StateFlow<String> = _newMealName.asStateFlow()

    private val _newMealComment = MutableStateFlow("")
    val newMealComment: StateFlow<String> = _newMealComment.asStateFlow()

    private val _newMealTimestamp = MutableStateFlow(System.currentTimeMillis())
    val newMealTimestamp: StateFlow<Long> = _newMealTimestamp.asStateFlow()

    private val _showAddMealDialog = MutableStateFlow(false)
    val showAddMealDialog: StateFlow<Boolean> = _showAddMealDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUserId = MutableStateFlow("")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    // Custom ingredients management
    private val _userIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val userIngredients: StateFlow<List<Ingredient>> = _userIngredients.asStateFlow()

    private val _showCreateIngredientDialog = MutableStateFlow(false)
    val showCreateIngredientDialog: StateFlow<Boolean> = _showCreateIngredientDialog.asStateFlow()

    private val _showMyIngredientsDialog = MutableStateFlow(false)
    val showMyIngredientsDialog: StateFlow<Boolean> = _showMyIngredientsDialog.asStateFlow()

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    private val _mealsForSelectedDate = MutableStateFlow<List<Meal>>(emptyList())
    val mealsForSelectedDate: StateFlow<List<Meal>> = _mealsForSelectedDate.asStateFlow()

    fun setCurrentUserId(userId: String) {
        _currentUserId.value = userId
        initializeData()
        // Only load meals for selected date, not all meals
        loadMealsForSelectedDate()
        loadDailyNutritionForDate(_selectedDate.value)
        loadUserIngredients()
    }

    fun setSelectedDate(date: Long) {
        _selectedDate.value = date
        loadMealsForSelectedDate()
        loadDailyNutritionForDate(date)
    }

    private fun initializeData() {
        viewModelScope.launch {
            initialData.initializeSampleIngredients()
        }
    }

    fun loadMeals() {
        val userId = _currentUserId.value
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                mealRepository.getAllMeals(userId).collect { meals ->
                    _allMeals.value = meals
                }
            }
        }
    }

    fun loadDailyNutrition() {
        val userId = _currentUserId.value
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                val today = System.currentTimeMillis()
                val nutrition = mealRepository.getDailyNutrition(userId, today)
                _dailyNutrition.value = nutrition
            }
        }
    }

    fun loadDailyNutritionForDate(date: Long) {
        val userId = _currentUserId.value
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                val nutrition = mealRepository.getDailyNutrition(userId, date)
                _dailyNutrition.value = nutrition
            }
        }
    }

    fun loadMealsForSelectedDate() {
        val userId = _currentUserId.value
        val selectedDate = _selectedDate.value
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                mealRepository.getMealsByDate(userId, selectedDate).collect { meals ->
                    _mealsForSelectedDate.value = meals
                }
            }
        }
    }

    fun searchIngredients(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                mealRepository.searchIngredients(query).collect { ingredients ->
                    _searchResults.value = ingredients
                }
            }
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun addIngredient(ingredient: Ingredient, quantity: Double) {
        val mealIngredient = MealIngredient(
            mealId = 0L, // Will be set when meal is created
            ingredient = ingredient,
            quantity = quantity
        )
        _selectedIngredients.value = _selectedIngredients.value + mealIngredient
    }

    fun removeIngredient(mealIngredient: MealIngredient) {
        _selectedIngredients.value = _selectedIngredients.value.filter { it != mealIngredient }
    }

    fun updateIngredientQuantity(mealIngredient: MealIngredient, newQuantity: Double) {
        _selectedIngredients.value = _selectedIngredients.value.map { ingredient ->
            if (ingredient == mealIngredient) {
                ingredient.copy(quantity = newQuantity)
            } else {
                ingredient
            }
        }
    }

    fun setNewMealName(name: String) {
        _newMealName.value = name
    }

    fun setNewMealComment(comment: String) {
        _newMealComment.value = comment
    }

    fun setNewMealTimestamp(timestamp: Long) {
        _newMealTimestamp.value = timestamp
    }

    fun setShowAddMealDialog(show: Boolean) {
        _showAddMealDialog.value = show
        if (show) {
            // Initialize with current date when opening dialog
            _newMealTimestamp.value = System.currentTimeMillis()
        } else {
            // Reset form when dialog is closed
            _newMealName.value = ""
            _newMealComment.value = ""
            _selectedIngredients.value = emptyList()
        }
    }

    fun submitNewMeal() {
        val userId = _currentUserId.value
        if (userId.isEmpty()) return

        val ingredients = _selectedIngredients.value
        if (ingredients.isEmpty()) return

        // Calculate nutrition from ingredients
        val totalCalories = ingredients.sumOf { (it.ingredient.caloriesPer100g * it.quantity / 100).toInt() }
        val totalCarbs = ingredients.sumOf { it.ingredient.carbsPer100g * it.quantity / 100 }
        val totalProtein = ingredients.sumOf { it.ingredient.proteinPer100g * it.quantity / 100 }
        val totalFat = ingredients.sumOf { it.ingredient.fatPer100g * it.quantity / 100 }
        val totalSugar = ingredients.sumOf { it.ingredient.sugarPer100g * it.quantity / 100 }
        val totalSalt = ingredients.sumOf { it.ingredient.saltPer100g * it.quantity / 100 }

        val meal = Meal(
            name = _newMealName.value,
            timestamp = _newMealTimestamp.value,
            calories = totalCalories,
            carbs = totalCarbs,
            protein = totalProtein,
            fat = totalFat,
            sugar = totalSugar,
            salt = totalSalt,
            comment = _newMealComment.value.takeIf { it.isNotEmpty() },
            userId = userId,
            ingredients = ingredients
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                mealRepository.insertMeal(meal)
                setShowAddMealDialog(false)
                // Refresh meals for selected date and daily nutrition
                loadMealsForSelectedDate()
                loadDailyNutritionForDate(_selectedDate.value)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                mealRepository.deleteMeal(meal)
                // Refresh meals for selected date and daily nutrition
                loadMealsForSelectedDate()
                loadDailyNutritionForDate(_selectedDate.value)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Custom ingredients management
    fun loadUserIngredients() {
        val userId = _currentUserId.value
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                mealRepository.getUserIngredients(userId).collect { ingredients ->
                    _userIngredients.value = ingredients
                }
            }
        }
    }

    fun setShowCreateIngredientDialog(show: Boolean) {
        _showCreateIngredientDialog.value = show
    }

    fun setShowMyIngredientsDialog(show: Boolean) {
        _showMyIngredientsDialog.value = show
    }

    fun createCustomIngredient(ingredient: Ingredient) {
        val userId = _currentUserId.value
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val customIngredient = ingredient.copy(userId = userId)
                    mealRepository.insertIngredient(customIngredient)
                    loadUserIngredients() // Refresh the list
                    setShowCreateIngredientDialog(false)
                } catch (e: Exception) {
                    // Handle error
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun deleteCustomIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            try {
                mealRepository.deleteIngredient(ingredient)
                loadUserIngredients() // Refresh the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}