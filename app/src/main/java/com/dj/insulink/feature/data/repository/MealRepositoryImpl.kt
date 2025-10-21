package com.dj.insulink.feature.data.repository

import android.util.Log
import com.dj.insulink.feature.data.room.dao.IngredientDao
import com.dj.insulink.feature.data.room.dao.MealDao
import com.dj.insulink.feature.data.room.dao.MealIngredientDao
import com.dj.insulink.feature.domain.mappers.toDomain
import com.dj.insulink.feature.domain.mappers.toEntity
import com.dj.insulink.feature.domain.models.DailyNutrition
import com.dj.insulink.feature.domain.models.Ingredient
import com.dj.insulink.feature.domain.models.Meal
import com.dj.insulink.feature.domain.models.MealIngredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealRepositoryImpl @Inject constructor(
    private val mealDao: MealDao,
    private val ingredientDao: IngredientDao,
    private val mealIngredientDao: MealIngredientDao
) : MealRepository {

    override fun getAllMeals(userId: String): Flow<List<Meal>> {
        return mealDao.getAllMeals(userId).map { mealEntities ->
            mealEntities.map { mealEntity ->
                val ingredients = getMealIngredients(mealEntity.id)
                mealEntity.toDomain(ingredients)
            }
        }
    }

    override fun getMealsByDate(userId: String, date: Long): Flow<List<Meal>> {
        return mealDao.getMealsByDate(userId, date).map { mealEntities ->
            mealEntities.map { mealEntity ->
                val ingredients = getMealIngredients(mealEntity.id)
                mealEntity.toDomain(ingredients)
            }
        }
    }

    override suspend fun insertMeal(meal: Meal) {
        try {
            val mealId = mealDao.insertMeal(meal.toEntity())
            Log.d("MealRepository", "Meal inserted with ID: $mealId")
            
            // Insert ingredients
            meal.ingredients.forEach { mealIngredient ->
                val ingredientEntity = mealIngredient.ingredient.toEntity()
                val ingredientId = if (ingredientEntity.id == 0L) {
                    ingredientDao.insertIngredient(ingredientEntity)
                } else {
                    ingredientEntity.id
                }
                
                val mealIngredientEntity = mealIngredient.copy(
                    mealId = mealId,
                    ingredient = mealIngredient.ingredient.copy(id = ingredientId)
                ).toEntity()
                
                mealIngredientDao.insertMealIngredient(mealIngredientEntity)
            }
        } catch (e: Exception) {
            Log.e("MealRepository", "Error inserting meal", e)
            throw e
        }
    }

    override suspend fun updateMeal(meal: Meal) {
        try {
            mealDao.updateMeal(meal.toEntity())
            
            // Update ingredients
            mealIngredientDao.deleteIngredientsForMeal(meal.id)
            meal.ingredients.forEach { mealIngredient ->
                val ingredientEntity = mealIngredient.ingredient.toEntity()
                val ingredientId = if (ingredientEntity.id == 0L) {
                    ingredientDao.insertIngredient(ingredientEntity)
                } else {
                    ingredientEntity.id
                }
                
                val mealIngredientEntity = mealIngredient.copy(
                    mealId = meal.id,
                    ingredient = mealIngredient.ingredient.copy(id = ingredientId)
                ).toEntity()
                
                mealIngredientDao.insertMealIngredient(mealIngredientEntity)
            }
        } catch (e: Exception) {
            Log.e("MealRepository", "Error updating meal", e)
            throw e
        }
    }

    override suspend fun deleteMeal(meal: Meal) {
        try {
            mealIngredientDao.deleteIngredientsForMeal(meal.id)
            mealDao.deleteMeal(meal.toEntity())
        } catch (e: Exception) {
            Log.e("MealRepository", "Error deleting meal", e)
            throw e
        }
    }

    override suspend fun getDailyNutrition(userId: String, date: Long): DailyNutrition {
        val calories = mealDao.getTotalCaloriesForDate(userId, date) ?: 0
        val carbs = mealDao.getTotalCarbsForDate(userId, date) ?: 0.0
        val protein = mealDao.getTotalProteinForDate(userId, date) ?: 0.0
        val fat = mealDao.getTotalFatForDate(userId, date) ?: 0.0
        val sugar = mealDao.getTotalSugarForDate(userId, date) ?: 0.0
        val salt = mealDao.getTotalSaltForDate(userId, date) ?: 0.0
        
        return DailyNutrition(
            calories = calories,
            carbs = carbs.toInt(),
            protein = protein.toInt(),
            fat = fat.toInt(),
            sugar = sugar.toInt(),
            salt = salt
        )
    }

    override fun searchIngredients(query: String, userId: String): Flow<List<Ingredient>> {
        return ingredientDao.searchIngredients(query, userId).map { ingredientEntities ->
            ingredientEntities.map { it.toDomain() }
        }
    }

    override suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredient(ingredient.toEntity())
    }

    override suspend fun getIngredientById(id: Long): Ingredient? {
        return ingredientDao.getIngredientById(id)?.toDomain()
    }

    override fun getUserIngredients(userId: String): Flow<List<Ingredient>> {
        return ingredientDao.getUserIngredients(userId).map { ingredientEntities ->
            ingredientEntities.map { it.toDomain() }
        }
    }

    override suspend fun updateIngredient(ingredient: Ingredient) {
        ingredientDao.updateIngredient(ingredient.toEntity())
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        ingredientDao.deleteIngredient(ingredient.toEntity())
    }

    private suspend fun getMealIngredients(mealId: Long): List<MealIngredient> {
        val mealIngredientEntities = mealIngredientDao.getIngredientsForMealSync(mealId)
        return mealIngredientEntities.mapNotNull { mealIngredientEntity ->
            val ingredient = ingredientDao.getIngredientById(mealIngredientEntity.ingredientId)?.toDomain()
            ingredient?.let { mealIngredientEntity.toDomain(it) }
        }
    }
}
