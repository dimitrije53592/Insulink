package com.dj.insulink.feature.meals.data.api

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    @SerializedName("results")
    val results: List<FoodItem> = emptyList()
)

data class FoodItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("imageType")
    val imageType: String? = null,
    @SerializedName("nutrition")
    val nutrition: FoodNutrition? = null
)

data class FoodNutrition(
    @SerializedName("nutrients")
    val nutrients: List<Nutrient> = emptyList()
)

data class Nutrient(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("unit")
    val unit: String = ""
)

data class UsdaFoodSearchResponse(
    @SerializedName("foods")
    val foods: List<UsdaFoodItem> = emptyList()
)

data class UsdaFoodItem(
    @SerializedName("fdcId")
    val fdcId: Int = 0,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("dataType")
    val dataType: String? = null,
    @SerializedName("foodNutrients")
    val foodNutrients: List<UsdaFoodNutrient>? = null
)

data class UsdaFoodNutrient(
    @SerializedName("nutrientId")
    val nutrientId: Int = 0,
    @SerializedName("nutrientName")
    val nutrientName: String = "",
    @SerializedName("value")
    val value: Double = 0.0,
    @SerializedName("unitName")
    val unitName: String = ""
)
