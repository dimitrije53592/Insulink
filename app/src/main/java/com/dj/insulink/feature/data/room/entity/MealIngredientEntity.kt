package com.dj.insulink.feature.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_ingredients")
data class MealIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mealId: Long,
    val ingredientId: Long,
    val quantity: Double, // in grams
    val firebaseId: String? = null, // For Firebase sync
    val createdAt: Long = System.currentTimeMillis()
)
