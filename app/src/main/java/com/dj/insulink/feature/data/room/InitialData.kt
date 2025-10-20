package com.dj.insulink.feature.data.room

import com.dj.insulink.feature.data.room.dao.IngredientDao
import com.dj.insulink.feature.data.room.entity.IngredientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitialData @Inject constructor(
    private val ingredientDao: IngredientDao
) {
    
    suspend fun initializeSampleIngredients() = withContext(Dispatchers.IO) {
        val sampleIngredients = listOf(
            IngredientEntity(
                name = "Chicken Breast",
                caloriesPer100g = 165.0,
                proteinPer100g = 31.0,
                carbsPer100g = 0.0,
                fatPer100g = 3.6,
                sugarPer100g = 0.0,
                saltPer100g = 0.1
            ),
            IngredientEntity(
                name = "Brown Rice",
                caloriesPer100g = 111.0,
                proteinPer100g = 2.6,
                carbsPer100g = 23.0,
                fatPer100g = 0.9,
                sugarPer100g = 0.4,
                saltPer100g = 0.0
            ),
            IngredientEntity(
                name = "Broccoli",
                caloriesPer100g = 34.0,
                proteinPer100g = 2.8,
                carbsPer100g = 7.0,
                fatPer100g = 0.4,
                sugarPer100g = 1.5,
                saltPer100g = 0.0
            ),
            IngredientEntity(
                name = "Salmon",
                caloriesPer100g = 208.0,
                proteinPer100g = 25.0,
                carbsPer100g = 0.0,
                fatPer100g = 12.0,
                sugarPer100g = 0.0,
                saltPer100g = 0.1
            ),
            IngredientEntity(
                name = "Sweet Potato",
                caloriesPer100g = 86.0,
                proteinPer100g = 1.6,
                carbsPer100g = 20.0,
                fatPer100g = 0.1,
                sugarPer100g = 4.2,
                saltPer100g = 0.0
            ),
            IngredientEntity(
                name = "Avocado",
                caloriesPer100g = 160.0,
                proteinPer100g = 2.0,
                carbsPer100g = 9.0,
                fatPer100g = 15.0,
                sugarPer100g = 0.7,
                saltPer100g = 0.0
            ),
            IngredientEntity(
                name = "Eggs",
                caloriesPer100g = 155.0,
                proteinPer100g = 13.0,
                carbsPer100g = 1.1,
                fatPer100g = 11.0,
                sugarPer100g = 1.1,
                saltPer100g = 0.1
            ),
            IngredientEntity(
                name = "Oatmeal",
                caloriesPer100g = 68.0,
                proteinPer100g = 2.4,
                carbsPer100g = 12.0,
                fatPer100g = 1.4,
                sugarPer100g = 0.0,
                saltPer100g = 0.0
            ),
            IngredientEntity(
                name = "Greek Yogurt",
                caloriesPer100g = 59.0,
                proteinPer100g = 10.0,
                carbsPer100g = 3.6,
                fatPer100g = 0.4,
                sugarPer100g = 3.6,
                saltPer100g = 0.1
            ),
            IngredientEntity(
                name = "Almonds",
                caloriesPer100g = 579.0,
                proteinPer100g = 21.0,
                carbsPer100g = 22.0,
                fatPer100g = 50.0,
                sugarPer100g = 4.4,
                saltPer100g = 0.0
            )
        )
        
        sampleIngredients.forEach { ingredient ->
            try {
                ingredientDao.insertIngredient(ingredient)
            } catch (e: Exception) {
                // Ingredient might already exist, ignore
            }
        }
    }
}
