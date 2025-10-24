package com.dj.insulink.feature.data.repository

import android.util.Log
import com.dj.insulink.BuildConfig
import com.dj.insulink.feature.data.api.FoodApiService
import com.dj.insulink.feature.data.api.FoodItem
import com.dj.insulink.feature.data.api.UsdaFoodItem
import com.dj.insulink.feature.domain.models.Ingredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodApiRepository @Inject constructor(
    private val foodApiService: FoodApiService
) {
    
    private val spoonacularApiKey = BuildConfig.SPOONACULAR_API_KEY
    private val usdaApiKey = BuildConfig.USDA_API_KEY
    
    suspend fun searchFoods(query: String): List<Ingredient> {
        return withContext(Dispatchers.IO) {
            try {
                // Try USDA FoodData Central API first (better nutritional data)
                val response = foodApiService.searchUsdaFoods(
                    query = query,
                    pageSize = 10,
                    apiKey = usdaApiKey
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val foodItems = response.body()!!.foods
                    return@withContext convertUsdaFoodItemsToIngredients(foodItems)
                } else {
                    Log.w("FoodApiRepository", "USDA API failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FoodApiRepository", "Error searching foods with USDA", e)
            }
            
            // Fallback to Spoonacular if USDA fails
            try {
                val response = foodApiService.searchIngredients(
                    query = query,
                    number = 10,
                    addRecipeNutrition = true,
                    apiKey = spoonacularApiKey
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val foodItems = response.body()!!.results
                    return@withContext convertFoodItemsToIngredients(foodItems)
                } else {
                    Log.w("FoodApiRepository", "Spoonacular API failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FoodApiRepository", "Error searching foods with Spoonacular", e)
            }
            
            // Fallback to empty list if both APIs fail
            emptyList()
        }
    }
    
    suspend fun searchUsdaFoods(query: String): List<Ingredient> {
        return withContext(Dispatchers.IO) {
            try {
                val response = foodApiService.searchUsdaFoods(
                    query = query,
                    pageSize = 10,
                    apiKey = usdaApiKey
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val foodItems = response.body()!!.foods
                    return@withContext convertUsdaFoodItemsToIngredients(foodItems)
                } else {
                    Log.w("FoodApiRepository", "USDA API failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FoodApiRepository", "Error searching foods with USDA", e)
            }
            
            // Fallback to empty list if API fails
            emptyList()
        }
    }
    
    private fun convertFoodItemsToIngredients(foodItems: List<FoodItem>): List<Ingredient> {
        return foodItems.mapNotNull { foodItem ->
            // Skip items with null or empty names
            val name = foodItem.title?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            
            val nutrition = foodItem.nutrition?.nutrients ?: emptyList()
            
            // Extract nutritional values
            val calories = nutrition.find { it.name.equals("Calories", ignoreCase = true) }?.amount ?: 0.0
            val protein = nutrition.find { it.name.equals("Protein", ignoreCase = true) }?.amount ?: 0.0
            val carbs = nutrition.find { it.name.equals("Carbohydrates", ignoreCase = true) }?.amount ?: 0.0
            val fat = nutrition.find { it.name.equals("Fat", ignoreCase = true) }?.amount ?: 0.0
            val sugar = nutrition.find { it.name.equals("Sugar", ignoreCase = true) }?.amount ?: 0.0
            val salt = nutrition.find { it.name.equals("Sodium", ignoreCase = true) }?.amount ?: 0.0
            
            Ingredient(
                id = 0L, // Will be set when saved to database
                name = name,
                caloriesPer100g = calories,
                proteinPer100g = protein,
                carbsPer100g = carbs,
                fatPer100g = fat,
                sugarPer100g = sugar,
                saltPer100g = salt / 1000.0, // Convert mg to g
                userId = null, // System ingredient
                firebaseId = null,
                createdAt = System.currentTimeMillis()
            )
        }
    }
    
    private fun convertUsdaFoodItemsToIngredients(foodItems: List<UsdaFoodItem>): List<Ingredient> {
        return foodItems.mapNotNull { foodItem ->
            // Skip items with null or empty names
            val name = foodItem.description?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            
            val nutrients = foodItem.foodNutrients ?: emptyList()
            
            // Extract nutritional values (USDA uses different nutrient IDs)
            // Updated to use correct field names from actual API response
            val calories = nutrients.find { it.nutrientId == 1008 }?.value ?: 0.0 // Energy
            val protein = nutrients.find { it.nutrientId == 1003 }?.value ?: 0.0 // Protein
            val carbs = nutrients.find { it.nutrientId == 1005 }?.value ?: 0.0 // Carbohydrates
            val fat = nutrients.find { it.nutrientId == 1004 }?.value ?: 0.0 // Fat
            val sugar = nutrients.find { it.nutrientId == 2000 }?.value ?: 0.0 // Sugars
            val salt = nutrients.find { it.nutrientId == 1093 }?.value ?: 0.0 // Sodium
            
            Ingredient(
                id = 0L, // Will be set when saved to database
                name = name,
                caloriesPer100g = calories,
                proteinPer100g = protein,
                carbsPer100g = carbs,
                fatPer100g = fat,
                sugarPer100g = sugar,
                saltPer100g = salt / 1000.0, // Convert mg to g
                userId = null, // System ingredient
                firebaseId = null,
                createdAt = System.currentTimeMillis()
            )
        }
    }
}
