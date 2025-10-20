package com.dj.insulink.feature.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val sugarPer100g: Double,
    val saltPer100g: Double,
    val firebaseId: String? = null, // For Firebase sync
    val createdAt: Long = System.currentTimeMillis()
)
