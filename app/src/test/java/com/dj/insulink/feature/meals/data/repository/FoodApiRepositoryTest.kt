package com.dj.insulink.feature.meals.data.repository

import com.dj.insulink.feature.meals.data.api.FoodApiService
import com.dj.insulink.feature.meals.data.api.FoodItem
import com.dj.insulink.feature.meals.data.api.FoodNutrition
import com.dj.insulink.feature.meals.data.api.FoodSearchResponse
import com.dj.insulink.feature.meals.data.api.Nutrient
import com.dj.insulink.feature.meals.data.api.UsdaFoodItem
import com.dj.insulink.feature.meals.data.api.UsdaFoodNutrient
import com.dj.insulink.feature.meals.data.api.UsdaFoodSearchResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class FoodApiRepositoryTest {

    private lateinit var usdaService: FoodApiService
    private lateinit var spoonacularService: FoodApiService
    private lateinit var repository: FoodApiRepository

    @Before
    fun setUp() {
        usdaService = mockk()
        spoonacularService = mockk()
        repository = FoodApiRepository(usdaService, spoonacularService)
    }

    private fun setApiKeys(usda: String, spoonacular: String) {
        setPrivateField(repository, "usdaApiKey", usda)
        setPrivateField(repository, "spoonacularApiKey", spoonacular)
    }

    private fun setPrivateField(target: Any, name: String, value: Any?) {
        val field = target.javaClass.getDeclaredField(name)
        field.isAccessible = true
        field.set(target, value)
    }

    private fun <T> errorResponse(): Response<T> =
        Response.error(500, "boom".toResponseBody("text/plain".toMediaTypeOrNull()))

    @Test
    fun `usda success maps nutrients by id and converts sodium to grams`() = runTest {
        setApiKeys(usda = "usda-key", spoonacular = "")
        val usdaFood = UsdaFoodItem(
            description = "Banana",
            foodNutrients = listOf(
                UsdaFoodNutrient(nutrientId = 1008, value = 89.0),
                UsdaFoodNutrient(nutrientId = 1003, value = 1.1),
                UsdaFoodNutrient(nutrientId = 1005, value = 22.8),
                UsdaFoodNutrient(nutrientId = 1004, value = 0.3),
                UsdaFoodNutrient(nutrientId = 2000, value = 12.2),
                UsdaFoodNutrient(nutrientId = 1093, value = 1000.0) // mg sodium -> 1.0 g salt
            )
        )
        coEvery { usdaService.searchUsdaFoods(any(), any(), any()) } returns
            Response.success(UsdaFoodSearchResponse(foods = listOf(usdaFood)))

        val result = repository.searchFoods("banana")

        assertEquals(1, result.size)
        val ingredient = result.first()
        assertEquals("Banana", ingredient.name)
        assertEquals(89.0, ingredient.caloriesPer100g, 0.0001)
        assertEquals(1.1, ingredient.proteinPer100g, 0.0001)
        assertEquals(22.8, ingredient.carbsPer100g, 0.0001)
        assertEquals(0.3, ingredient.fatPer100g, 0.0001)
        assertEquals(12.2, ingredient.sugarPer100g, 0.0001)
        assertEquals(1.0, ingredient.saltPer100g, 0.0001)
        coVerify(exactly = 0) { spoonacularService.searchIngredients(any(), any(), any(), any()) }
    }

    @Test
    fun `usda items with null or blank names are dropped`() = runTest {
        setApiKeys(usda = "usda-key", spoonacular = "")
        coEvery { usdaService.searchUsdaFoods(any(), any(), any()) } returns Response.success(
            UsdaFoodSearchResponse(
                foods = listOf(
                    UsdaFoodItem(description = null),
                    UsdaFoodItem(description = "   "),
                    UsdaFoodItem(description = "Apple")
                )
            )
        )

        val result = repository.searchFoods("a")

        assertEquals(listOf("Apple"), result.map { it.name })
    }

    @Test
    fun `usda missing nutrients default to zero`() = runTest {
        setApiKeys(usda = "usda-key", spoonacular = "")
        coEvery { usdaService.searchUsdaFoods(any(), any(), any()) } returns Response.success(
            UsdaFoodSearchResponse(foods = listOf(UsdaFoodItem(description = "Water", foodNutrients = null)))
        )

        val ingredient = repository.searchFoods("water").first()

        assertEquals(0.0, ingredient.caloriesPer100g, 0.0001)
        assertEquals(0.0, ingredient.saltPer100g, 0.0001)
    }

    @Test
    fun `falls back to spoonacular when usda response is unsuccessful`() = runTest {
        setApiKeys(usda = "usda-key", spoonacular = "spoon-key")
        coEvery { usdaService.searchUsdaFoods(any(), any(), any()) } returns errorResponse()
        coEvery { spoonacularService.searchIngredients(any(), any(), any(), any()) } returns Response.success(
            FoodSearchResponse(
                results = listOf(
                    FoodItem(
                        title = "Egg",
                        nutrition = FoodNutrition(
                            nutrients = listOf(
                                Nutrient(name = "Calories", amount = 155.0),
                                Nutrient(name = "Protein", amount = 13.0),
                                Nutrient(name = "Carbohydrates", amount = 1.1),
                                Nutrient(name = "Fat", amount = 11.0),
                                Nutrient(name = "Sugar", amount = 1.1),
                                Nutrient(name = "Sodium", amount = 124.0)
                            )
                        )
                    )
                )
            )
        )

        val result = repository.searchFoods("egg")

        assertEquals(1, result.size)
        val ingredient = result.first()
        assertEquals("Egg", ingredient.name)
        assertEquals(155.0, ingredient.caloriesPer100g, 0.0001)
        assertEquals(0.124, ingredient.saltPer100g, 0.0001) // 124 mg -> 0.124 g
    }

    @Test
    fun `falls back to spoonacular when usda call throws`() = runTest {
        setApiKeys(usda = "usda-key", spoonacular = "spoon-key")
        coEvery { usdaService.searchUsdaFoods(any(), any(), any()) } throws RuntimeException("network")
        coEvery { spoonacularService.searchIngredients(any(), any(), any(), any()) } returns
            Response.success(FoodSearchResponse(results = listOf(FoodItem(title = "Rice"))))

        val result = repository.searchFoods("rice")

        assertEquals(listOf("Rice"), result.map { it.name })
    }

    @Test
    fun `returns empty list when both providers fail`() = runTest {
        setApiKeys(usda = "usda-key", spoonacular = "spoon-key")
        coEvery { usdaService.searchUsdaFoods(any(), any(), any()) } returns errorResponse()
        coEvery { spoonacularService.searchIngredients(any(), any(), any(), any()) } returns errorResponse()

        assertTrue(repository.searchFoods("nothing").isEmpty())
    }

    @Test
    fun `returns empty list when no api keys are configured`() = runTest {
        setApiKeys(usda = "", spoonacular = "")

        assertTrue(repository.searchFoods("anything").isEmpty())

        coVerify(exactly = 0) { usdaService.searchUsdaFoods(any(), any(), any()) }
        coVerify(exactly = 0) { spoonacularService.searchIngredients(any(), any(), any(), any()) }
    }
}
