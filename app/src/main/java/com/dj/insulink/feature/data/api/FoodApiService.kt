package com.dj.insulink.feature.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {
    
    // Spoonacular API endpoints
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("addRecipeNutrition") addRecipeNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): Response<FoodSearchResponse>
    
    @GET("food/ingredients/search")
    suspend fun searchIngredients(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("addRecipeNutrition") addRecipeNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): Response<FoodSearchResponse>
    
    // USDA FoodData Central API endpoints (alternative)
    @GET("fdc/v1/foods/search")
    suspend fun searchUsdaFoods(
        @Query("query") query: String,
        @Query("pageSize") pageSize: Int = 10,
        @Query("api_key") apiKey: String
    ): Response<UsdaFoodSearchResponse>
}
