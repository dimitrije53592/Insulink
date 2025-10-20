package com.dj.insulink.feature.data.room.dao

import androidx.room.*
import com.dj.insulink.feature.data.room.entity.MealIngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealIngredientDao {
    @Query("SELECT * FROM meal_ingredients WHERE mealId = :mealId")
    fun getIngredientsForMeal(mealId: Long): Flow<List<MealIngredientEntity>>

    @Query("SELECT * FROM meal_ingredients WHERE mealId = :mealId")
    suspend fun getIngredientsForMealSync(mealId: Long): List<MealIngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealIngredient(mealIngredient: MealIngredientEntity): Long

    @Update
    suspend fun updateMealIngredient(mealIngredient: MealIngredientEntity)

    @Delete
    suspend fun deleteMealIngredient(mealIngredient: MealIngredientEntity)

    @Query("DELETE FROM meal_ingredients WHERE mealId = :mealId")
    suspend fun deleteIngredientsForMeal(mealId: Long)

    @Query("DELETE FROM meal_ingredients WHERE id = :id")
    suspend fun deleteMealIngredientById(id: Long)
}
