package com.dj.insulink.feature.domain.mappers

import com.dj.insulink.feature.data.room.entity.IngredientEntity
import com.dj.insulink.feature.data.room.entity.MealEntity
import com.dj.insulink.feature.data.room.entity.MealIngredientEntity
import com.dj.insulink.feature.domain.models.Ingredient
import com.dj.insulink.feature.domain.models.Meal
import com.dj.insulink.feature.domain.models.MealIngredient

fun MealEntity.toDomain(ingredients: List<MealIngredient> = emptyList()): Meal {
    return Meal(
        id = id,
        name = name,
        timestamp = timestamp,
        calories = calories,
        carbs = carbs,
        protein = protein,
        fat = fat,
        sugar = sugar,
        salt = salt,
        comment = comment,
        userId = userId,
        firebaseId = firebaseId,
        ingredients = ingredients,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Meal.toEntity(): MealEntity {
    return MealEntity(
        id = id,
        name = name,
        timestamp = timestamp,
        calories = calories,
        carbs = carbs,
        protein = protein,
        fat = fat,
        sugar = sugar,
        salt = salt,
        comment = comment,
        userId = userId,
        firebaseId = firebaseId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun IngredientEntity.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        name = name,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        carbsPer100g = carbsPer100g,
        fatPer100g = fatPer100g,
        sugarPer100g = sugarPer100g,
        saltPer100g = saltPer100g,
        userId = userId,
        firebaseId = firebaseId,
        createdAt = createdAt
    )
}

fun Ingredient.toEntity(): IngredientEntity {
    return IngredientEntity(
        id = id,
        name = name,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        carbsPer100g = carbsPer100g,
        fatPer100g = fatPer100g,
        sugarPer100g = sugarPer100g,
        saltPer100g = saltPer100g,
        userId = userId,
        firebaseId = firebaseId,
        createdAt = createdAt
    )
}

fun MealIngredientEntity.toDomain(ingredient: Ingredient): MealIngredient {
    return MealIngredient(
        id = id,
        mealId = mealId,
        ingredient = ingredient,
        quantity = quantity,
        firebaseId = firebaseId,
        createdAt = createdAt
    )
}

fun MealIngredient.toEntity(): MealIngredientEntity {
    return MealIngredientEntity(
        id = id,
        mealId = mealId,
        ingredientId = ingredient.id,
        quantity = quantity,
        firebaseId = firebaseId,
        createdAt = createdAt
    )
}
