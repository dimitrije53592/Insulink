package com.dj.insulink.feature.meals.domain.mappers

import com.dj.insulink.feature.meals.data.room.entity.IngredientEntity
import com.dj.insulink.feature.meals.data.room.entity.MealEntity
import com.dj.insulink.feature.meals.data.room.entity.MealIngredientEntity
import com.dj.insulink.feature.meals.domain.model.Ingredient
import com.dj.insulink.feature.meals.domain.model.Meal
import com.dj.insulink.feature.meals.domain.model.MealIngredient
import org.junit.Assert.assertEquals
import org.junit.Test

class MealMappersTest {

    private val ingredient = Ingredient(
        id = 5, name = "Oats", caloriesPer100g = 389.0, proteinPer100g = 16.9,
        carbsPer100g = 66.3, fatPer100g = 6.9, sugarPer100g = 0.0, saltPer100g = 0.002,
        userId = "u1", firebaseId = "fb1", createdAt = 111L
    )

    private val meal = Meal(
        id = 1, name = "Breakfast", timestamp = 1000L, calories = 300, carbs = 40.0,
        protein = 12.0, fat = 5.0, sugar = 2.0, salt = 0.5, comment = "yum", userId = "u1",
        firebaseId = "fbm", ingredients = emptyList(), createdAt = 222L, updatedAt = 333L
    )

    @Test
    fun `ingredient round trips`() {
        assertEquals(ingredient, ingredient.toEntity().toDomain())
    }

    @Test
    fun `meal maps to entity preserving scalar fields`() {
        val entity = meal.toEntity()
        val expected = MealEntity(
            id = 1, name = "Breakfast", timestamp = 1000L, calories = 300, carbs = 40.0,
            protein = 12.0, fat = 5.0, sugar = 2.0, salt = 0.5, comment = "yum", userId = "u1",
            firebaseId = "fbm", createdAt = 222L, updatedAt = 333L
        )
        assertEquals(expected, entity)
    }

    @Test
    fun `meal entity maps to domain with provided ingredients`() {
        val mealIngredient = MealIngredient(id = 9, mealId = 1, ingredient = ingredient, quantity = 50.0, firebaseId = null, createdAt = 444L)
        val domain = meal.toEntity().toDomain(listOf(mealIngredient))
        assertEquals(meal.copy(ingredients = listOf(mealIngredient)), domain)
    }

    @Test
    fun `meal entity maps to domain with empty ingredients by default`() {
        assertEquals(meal, meal.toEntity().toDomain())
    }

    @Test
    fun `meal ingredient maps to entity using the ingredient id`() {
        val mealIngredient = MealIngredient(id = 9, mealId = 1, ingredient = ingredient, quantity = 50.0, firebaseId = "fbmi", createdAt = 444L)
        val expected = MealIngredientEntity(id = 9, mealId = 1, ingredientId = 5, quantity = 50.0, firebaseId = "fbmi", createdAt = 444L)
        assertEquals(expected, mealIngredient.toEntity())
    }

    @Test
    fun `meal ingredient entity maps to domain with supplied ingredient`() {
        val entity = MealIngredientEntity(id = 9, mealId = 1, ingredientId = 5, quantity = 50.0, firebaseId = "fbmi", createdAt = 444L)
        val expected = MealIngredient(id = 9, mealId = 1, ingredient = ingredient, quantity = 50.0, firebaseId = "fbmi", createdAt = 444L)
        assertEquals(expected, entity.toDomain(ingredient))
    }
}
