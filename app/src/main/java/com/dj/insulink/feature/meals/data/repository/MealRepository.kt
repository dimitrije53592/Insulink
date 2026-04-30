package com.dj.insulink.feature.meals.data.repository

import com.dj.insulink.feature.meals.domain.model.DailyNutrition
import com.dj.insulink.feature.meals.domain.model.Ingredient
import com.dj.insulink.feature.meals.domain.model.Meal
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun getAllMeals(userId: String): Flow<List<Meal>>
    fun getMealsByDate(userId: String, date: Long): Flow<List<Meal>>
    suspend fun insertMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    
    // Daily nutrition summary
    suspend fun getDailyNutrition(userId: String, date: Long): DailyNutrition
    
    // Ingredient management
    fun searchIngredients(query: String, userId: String): Flow<List<Ingredient>>
    suspend fun insertIngredient(ingredient: Ingredient)
    suspend fun getIngredientById(id: Long): Ingredient?
    fun getUserIngredients(userId: String): Flow<List<Ingredient>>
    suspend fun updateIngredient(ingredient: Ingredient)
    suspend fun deleteIngredient(ingredient: Ingredient)
}
