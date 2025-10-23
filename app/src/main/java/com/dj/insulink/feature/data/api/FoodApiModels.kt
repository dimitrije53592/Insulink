package com.dj.insulink.feature.data.api

import com.google.gson.annotations.SerializedName

// Spoonacular API Models
data class FoodSearchResponse(
    @SerializedName("results")
    val results: List<FoodItem>
)

data class FoodItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("nutrition")
    val nutrition: FoodNutrition? = null
)

data class FoodNutrition(
    @SerializedName("nutrients")
    val nutrients: List<Nutrient>
)

data class Nutrient(
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("unit")
    val unit: String
)

// Alternative: USDA FoodData Central Models (if we decide to use it)
data class UsdaFoodSearchResponse(
    @SerializedName("foods")
    val foods: List<UsdaFoodItem>
)

data class UsdaFoodItem(
    @SerializedName("fdcId")
    val fdcId: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("dataType")
    val dataType: String,
    @SerializedName("foodNutrients")
    val foodNutrients: List<UsdaFoodNutrient>? = null
)

data class UsdaFoodNutrient(
    @SerializedName("nutrientId")
    val nutrientId: Int,
    @SerializedName("nutrientName")
    val nutrientName: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("unitName")
    val unitName: String
)
