package com.dj.insulink.feature.meals.data.repository

import com.dj.insulink.feature.meals.data.room.dao.IngredientDao
import com.dj.insulink.feature.meals.data.room.dao.MealDao
import com.dj.insulink.feature.meals.data.room.dao.MealIngredientDao
import com.dj.insulink.feature.meals.data.room.entity.IngredientEntity
import com.dj.insulink.feature.meals.data.room.entity.MealEntity
import com.dj.insulink.feature.meals.data.room.entity.MealIngredientEntity
import com.dj.insulink.feature.meals.domain.model.DailyNutrition
import com.dj.insulink.feature.meals.domain.model.Ingredient
import com.dj.insulink.feature.meals.domain.model.Meal
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MealRepositoryTest {

    private val mealDao: MealDao = mockk(relaxed = true)
    private val mealIngredientDao: MealIngredientDao = mockk(relaxed = true)
    private val ingredientDao: IngredientDao = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk()
    private val foodApiRepository: FoodApiRepository = mockk()
    private lateinit var repository: MealRepository

    private fun ingredientEntity(id: Long, name: String) = IngredientEntity(
        id = id, name = name, caloriesPer100g = 100.0, proteinPer100g = 10.0,
        carbsPer100g = 20.0, fatPer100g = 5.0, sugarPer100g = 2.0, saltPer100g = 1.0,
        userId = "u1", firebaseId = null, createdAt = 0L
    )

    private fun apiIngredient(name: String) = Ingredient(
        id = 0, name = name, caloriesPer100g = 50.0, proteinPer100g = 1.0,
        carbsPer100g = 1.0, fatPer100g = 1.0, sugarPer100g = 1.0, saltPer100g = 0.1,
        userId = null, firebaseId = null, createdAt = 0L
    )

    @Before
    fun setUp() {
        repository = MealRepository(mealDao, mealIngredientDao, ingredientDao, firestore, foodApiRepository)
    }

    @Test
    fun `getDailyNutrition aggregates dao totals`() = runTest {
        coEvery { mealDao.getTotalCaloriesForDate("u1", 1L) } returns 300
        coEvery { mealDao.getTotalCarbsForDate("u1", 1L) } returns 40.0
        coEvery { mealDao.getTotalProteinForDate("u1", 1L) } returns 20.0
        coEvery { mealDao.getTotalFatForDate("u1", 1L) } returns 10.0
        coEvery { mealDao.getTotalSugarForDate("u1", 1L) } returns 5.0
        coEvery { mealDao.getTotalSaltForDate("u1", 1L) } returns 1.5

        assertEquals(DailyNutrition(300, 40, 20, 10, 5, 1.5), repository.getDailyNutrition("u1", 1L))
    }

    @Test
    fun `getDailyNutrition defaults nulls to zero`() = runTest {
        coEvery { mealDao.getTotalCaloriesForDate("u1", 1L) } returns null
        coEvery { mealDao.getTotalCarbsForDate("u1", 1L) } returns null
        coEvery { mealDao.getTotalProteinForDate("u1", 1L) } returns null
        coEvery { mealDao.getTotalFatForDate("u1", 1L) } returns null
        coEvery { mealDao.getTotalSugarForDate("u1", 1L) } returns null
        coEvery { mealDao.getTotalSaltForDate("u1", 1L) } returns null

        assertEquals(DailyNutrition(0, 0, 0, 0, 0, 0.0), repository.getDailyNutrition("u1", 1L))
    }

    @Test
    fun `insertIngredient stores via the ingredient dao`() = runTest {
        repository.insertIngredient(apiIngredient("Custom"))
        coVerify { ingredientDao.insertIngredient(match { it.name == "Custom" }) }
    }

    @Test
    fun `deleteIngredient removes via the ingredient dao`() = runTest {
        repository.deleteIngredient(apiIngredient("Custom"))
        coVerify { ingredientDao.deleteIngredient(match { it.name == "Custom" }) }
    }

    @Test
    fun `getUserIngredients maps entities to domain`() = runTest {
        every { ingredientDao.getUserIngredients("u1") } returns flowOf(listOf(ingredientEntity(5, "Oats")))

        val result = repository.getUserIngredients("u1").first()

        assertEquals(listOf("Oats"), result.map { it.name })
    }

    @Test
    fun `searchIngredients with a short query returns only local matches`() = runTest {
        every { ingredientDao.searchIngredients("oa", "u1") } returns flowOf(listOf(ingredientEntity(5, "Oats")))

        val result = repository.searchIngredients("oa", "u1").first()

        assertEquals(listOf("Oats"), result.map { it.name })
        coVerify(exactly = 0) { foodApiRepository.searchFoods(any()) }
    }

    @Test
    fun `searchIngredients merges unique api results for a longer query`() = runTest {
        every { ingredientDao.searchIngredients("oats", "u1") } returns flowOf(listOf(ingredientEntity(5, "Oats")))
        coEvery { foodApiRepository.searchFoods("oats") } returns listOf(apiIngredient("Oats"), apiIngredient("Apple"))

        val result = repository.searchIngredients("oats", "u1").first()

        // local "Oats" kept, api "Oats" dropped as duplicate, api "Apple" added
        assertEquals(listOf("Oats", "Apple"), result.map { it.name })
    }

    @Test
    fun `searchIngredients falls back to local results when the api throws`() = runTest {
        every { ingredientDao.searchIngredients("oats", "u1") } returns flowOf(listOf(ingredientEntity(5, "Oats")))
        coEvery { foodApiRepository.searchFoods("oats") } throws RuntimeException("network")

        val result = repository.searchIngredients("oats", "u1").first()

        assertEquals(listOf("Oats"), result.map { it.name })
    }

    @Test
    fun `getMealsByDateForUser maps meals together with their ingredients`() = runTest {
        val mealEntity = MealEntity(
            id = 1, name = "Lunch", timestamp = 1L, calories = 100, carbs = 1.0, protein = 1.0,
            fat = 1.0, sugar = 1.0, salt = 1.0, comment = null, userId = "u1", firebaseId = null,
            createdAt = 0L, updatedAt = 0L
        )
        every { mealDao.getMealsByDate("u1", 1L) } returns flowOf(listOf(mealEntity))
        coEvery { mealIngredientDao.getIngredientsForMealSync(1L) } returns
            listOf(MealIngredientEntity(id = 10, mealId = 1, ingredientId = 5, quantity = 50.0))
        coEvery { ingredientDao.getIngredientById(5L) } returns ingredientEntity(5, "Oats")

        val meals = repository.getMealsByDateForUser("u1", 1L).first()

        assertEquals(1, meals.size)
        assertEquals(1, meals.first().ingredients.size)
        assertEquals("Oats", meals.first().ingredients.first().ingredient.name)
    }

    @Test
    fun `insert stores the meal locally`() = runTest {
        coEvery { mealDao.insertMeal(any()) } returns 99L
        val meal = Meal(
            id = 0, name = "Lunch", timestamp = 1L, calories = 100, carbs = 1.0, protein = 1.0,
            fat = 1.0, sugar = 1.0, salt = 1.0, comment = null, userId = "u1", ingredients = emptyList()
        )

        repository.insert("u1", meal)

        coVerify { mealDao.insertMeal(match { it.name == "Lunch" && it.id != 0L }) }
    }

    @Test
    fun `delete removes the meal and its ingredients locally`() = runTest {
        val meal = Meal(
            id = 3, name = "Lunch", timestamp = 1L, calories = 100, carbs = 1.0, protein = 1.0,
            fat = 1.0, sugar = 1.0, salt = 1.0, comment = null, userId = "u1"
        )

        repository.delete("u1", meal)

        coVerify { mealIngredientDao.deleteIngredientsForMeal(3L) }
        coVerify { mealDao.deleteMeal(match { it.id == 3L }) }
    }
}
