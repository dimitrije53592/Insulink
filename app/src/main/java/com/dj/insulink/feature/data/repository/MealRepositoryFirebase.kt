package com.dj.insulink.feature.data.repository

import android.util.Log
import com.dj.insulink.feature.data.room.dao.MealDao
import com.dj.insulink.feature.data.room.dao.MealIngredientDao
import com.dj.insulink.feature.data.room.dao.IngredientDao
import com.dj.insulink.feature.domain.mappers.toDomain
import com.dj.insulink.feature.domain.mappers.toEntity
import com.dj.insulink.feature.domain.models.Meal
import com.dj.insulink.feature.domain.models.MealIngredient
import com.dj.insulink.feature.domain.models.Ingredient
import com.dj.insulink.feature.domain.models.DailyNutrition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.emptyList

class MealRepositoryFirebase @Inject constructor(
    private val mealDao: MealDao,
    private val mealIngredientDao: MealIngredientDao,
    private val ingredientDao: IngredientDao,
    private val firestore: FirebaseFirestore,
    private val foodApiRepository: FoodApiRepository
) {

    fun getAllMealsForUser(userId: String): Flow<List<Meal>> {
        return mealDao.getAllMeals(userId).map {
            it.map { mealEntity ->
                val ingredients = getMealIngredients(mealEntity.id)
                mealEntity.toDomain(ingredients)
            }
        }
    }

    fun getMealsByDateForUser(userId: String, date: Long): Flow<List<Meal>> {
        return mealDao.getMealsByDate(userId, date).map {
            it.map { mealEntity ->
                val ingredients = getMealIngredients(mealEntity.id)
                mealEntity.toDomain(ingredients)
            }
        }
    }

    suspend fun insert(userId: String, meal: Meal) {
        withContext(Dispatchers.IO) {
            try {
                val mealWithUniqueId = if (meal.id == 0L) {
                    meal.copy(id = System.currentTimeMillis())
                } else {
                    meal
                }

                // Insert meal to local database
                val mealId = mealDao.insertMeal(mealWithUniqueId.toEntity())
                
                // Insert meal ingredients
                mealWithUniqueId.ingredients.forEach { mealIngredient ->
                    val ingredientEntity = mealIngredient.ingredient.toEntity()
                    val ingredientId = if (ingredientEntity.id == 0L) {
                        ingredientDao.insertIngredient(ingredientEntity)
                    } else {
                        ingredientEntity.id
                    }
                    
                    val mealIngredientEntity = mealIngredient.copy(
                        mealId = mealId,
                        ingredient = mealIngredient.ingredient.copy(id = ingredientId)
                    ).toEntity()
                    
                    mealIngredientDao.insertMealIngredient(mealIngredientEntity)
                }

                // Push to Firebase
                pushMealToFirestoreForUser(userId, mealWithUniqueId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun delete(userId: String, meal: Meal) {
        withContext(Dispatchers.IO) {
            try {
                // Delete from local database
                mealIngredientDao.deleteIngredientsForMeal(meal.id)
                mealDao.deleteMeal(meal.toEntity())
                
                // Delete from Firebase
                deleteMealFromFirestoreForUser(userId, meal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun update(userId: String, meal: Meal) {
        withContext(Dispatchers.IO) {
            try {
                // Update local database
                mealDao.updateMeal(meal.toEntity())
                
                // Update ingredients
                mealIngredientDao.deleteIngredientsForMeal(meal.id)
                meal.ingredients.forEach { mealIngredient ->
                    val ingredientEntity = mealIngredient.ingredient.toEntity()
                    val ingredientId = if (ingredientEntity.id == 0L) {
                        ingredientDao.insertIngredient(ingredientEntity)
                    } else {
                        ingredientEntity.id
                    }
                    
                    val mealIngredientEntity = mealIngredient.copy(
                        mealId = meal.id,
                        ingredient = mealIngredient.ingredient.copy(id = ingredientId)
                    ).toEntity()
                    
                    mealIngredientDao.insertMealIngredient(mealIngredientEntity)
                }

                // Update in Firebase
                updateMealInFirestoreForUser(userId, meal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchAllMealsForUserAndUpdateDatabase(userId: String) {
        withContext(Dispatchers.IO) {
            val fetchedMeals = fetchMealsForUser(userId)
            mealDao.deleteAllForUser(userId)
            mealDao.insertAll(fetchedMeals.map { it.toEntity() })
        }
    }

    suspend fun getDailyNutrition(userId: String, date: Long): DailyNutrition {
        val calories = mealDao.getTotalCaloriesForDate(userId, date) ?: 0
        val carbs = mealDao.getTotalCarbsForDate(userId, date) ?: 0.0
        val protein = mealDao.getTotalProteinForDate(userId, date) ?: 0.0
        val fat = mealDao.getTotalFatForDate(userId, date) ?: 0.0
        val sugar = mealDao.getTotalSugarForDate(userId, date) ?: 0.0
        val salt = mealDao.getTotalSaltForDate(userId, date) ?: 0.0
        
        return DailyNutrition(
            calories = calories,
            carbs = carbs.toInt(),
            protein = protein.toInt(),
            fat = fat.toInt(),
            sugar = sugar.toInt(),
            salt = salt
        )
    }

    private suspend fun pushMealToFirestoreForUser(userId: String, meal: Meal) {
        try {
            val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)
            
            // First, try to get the existing document
            val snapshot = userDocumentRef.get().await()
            
            if (snapshot.exists()) {
                // Document exists, update it
                userDocumentRef.update(DOCUMENT_FIELD_MEALS, FieldValue.arrayUnion(meal)).await()
            } else {
                // Document doesn't exist, create it with the meal
                val userData = mapOf(
                    DOCUMENT_FIELD_MEALS to listOf(meal)
                )
                userDocumentRef.set(userData).await()
            }
        } catch (e: Exception) {
            Log.e("MealRepositoryFirebase", "Error pushing meal to Firestore", e)
        }
    }

    private suspend fun deleteMealFromFirestoreForUser(userId: String, meal: Meal) {
        try {
            val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)

            val snapshot = userDocumentRef.get().await()
            val meals = snapshot.get(DOCUMENT_FIELD_MEALS) as? List<Map<String, Any>> ?: emptyList()

            val updatedMeals = meals.filter { mealMap ->
                val idMatches = (mealMap["id"] as? Number)?.toLong() == meal.id
                !idMatches
            }

            if (meals.size != updatedMeals.size) {
                userDocumentRef.update(DOCUMENT_FIELD_MEALS, updatedMeals).await()
            } else {
                Log.w("MealRepositoryFirebase", "No meal found with ID ${meal.id} to delete")
            }
        } catch (e: Exception) {
            Log.e("MealRepositoryFirebase", "Error deleting meal from Firestore", e)
        }
    }

    private suspend fun updateMealInFirestoreForUser(userId: String, meal: Meal) {
        try {
            val userDocumentRef = firestore.collection(COLLECTION_NAME_USERS).document(userId)

            val snapshot = userDocumentRef.get().await()
            
            if (snapshot.exists()) {
                val meals = snapshot.get(DOCUMENT_FIELD_MEALS) as? List<Map<String, Any>> ?: emptyList()

                val updatedMeals = meals.map { mealMap ->
                    val idMatches = (mealMap["id"] as? Number)?.toLong() == meal.id
                    if (idMatches) {
                        meal
                    } else {
                        mealMap
                    }
                }

                userDocumentRef.update(DOCUMENT_FIELD_MEALS, updatedMeals).await()
            } else {
                // Document doesn't exist, create it with the meal
                val userData = mapOf(
                    DOCUMENT_FIELD_MEALS to listOf(meal)
                )
                userDocumentRef.set(userData).await()
            }
        } catch (e: Exception) {
            Log.e("MealRepositoryFirebase", "Error updating meal in Firestore", e)
        }
    }

    private suspend fun fetchMealsForUser(userId: String): List<Meal> {
        val document = firestore.collection(COLLECTION_NAME_USERS)
            .document(userId)
            .get()
            .await()

        if (!document.exists()) {
            return emptyList()
        }

        val mealsData = document.get("meals") as? List<Map<String, Any>> ?: emptyList()

        return mealsData.map { mealMap ->
            val ingredientsData = mealMap["ingredients"] as? List<Map<String, Any>> ?: emptyList()
            val ingredients = ingredientsData.map { ingredientMap ->
                val ingredientData = ingredientMap["ingredient"] as? Map<String, Any> ?: emptyMap()
                val ingredient = Ingredient(
                    id = (ingredientData["id"] as? Number)?.toLong() ?: 0,
                    name = ingredientData["name"] as? String ?: "",
                    caloriesPer100g = (ingredientData["caloriesPer100g"] as? Number)?.toDouble() ?: 0.0,
                    proteinPer100g = (ingredientData["proteinPer100g"] as? Number)?.toDouble() ?: 0.0,
                    carbsPer100g = (ingredientData["carbsPer100g"] as? Number)?.toDouble() ?: 0.0,
                    fatPer100g = (ingredientData["fatPer100g"] as? Number)?.toDouble() ?: 0.0,
                    sugarPer100g = (ingredientData["sugarPer100g"] as? Number)?.toDouble() ?: 0.0,
                    saltPer100g = (ingredientData["saltPer100g"] as? Number)?.toDouble() ?: 0.0,
                    userId = ingredientData["userId"] as? String,
                    firebaseId = ingredientData["firebaseId"] as? String,
                    createdAt = (ingredientData["createdAt"] as? Number)?.toLong() ?: 0
                )
                MealIngredient(
                    id = (ingredientMap["id"] as? Number)?.toLong() ?: 0,
                    mealId = (ingredientMap["mealId"] as? Number)?.toLong() ?: 0,
                    ingredient = ingredient,
                    quantity = (ingredientMap["quantity"] as? Number)?.toDouble() ?: 0.0,
                    firebaseId = ingredientMap["firebaseId"] as? String,
                    createdAt = (ingredientMap["createdAt"] as? Number)?.toLong() ?: 0
                )
            }

            Meal(
                id = (mealMap["id"] as? Number)?.toLong() ?: 0,
                name = mealMap["name"] as? String ?: "",
                timestamp = (mealMap["timestamp"] as? Number)?.toLong() ?: 0,
                calories = (mealMap["calories"] as? Number)?.toInt(),
                carbs = (mealMap["carbs"] as? Number)?.toDouble(),
                protein = (mealMap["protein"] as? Number)?.toDouble(),
                fat = (mealMap["fat"] as? Number)?.toDouble(),
                sugar = (mealMap["sugar"] as? Number)?.toDouble(),
                salt = (mealMap["salt"] as? Number)?.toDouble(),
                comment = mealMap["comment"] as? String,
                userId = mealMap["userId"] as? String ?: "",
                firebaseId = mealMap["firebaseId"] as? String,
                ingredients = ingredients,
                createdAt = (mealMap["createdAt"] as? Number)?.toLong() ?: 0,
                updatedAt = (mealMap["updatedAt"] as? Number)?.toLong() ?: 0
            )
        }
    }

    private suspend fun getMealIngredients(mealId: Long): List<MealIngredient> {
        val mealIngredientEntities = mealIngredientDao.getIngredientsForMealSync(mealId)
        return mealIngredientEntities.mapNotNull { mealIngredientEntity ->
            val ingredient = ingredientDao.getIngredientById(mealIngredientEntity.ingredientId)?.toDomain()
            ingredient?.let { mealIngredientEntity.toDomain(it) }
        }
    }

    // Ingredient management methods
    fun searchIngredients(query: String, userId: String): Flow<List<Ingredient>> {
        return kotlinx.coroutines.flow.flow {
            // First emit local results
            val localIngredients = ingredientDao.searchIngredients(query, userId).map { ingredientEntities ->
                ingredientEntities.map { it.toDomain() }
            }
            
            localIngredients.collect { local ->
                // If query is too short, only show local results
                if (query.length < 3) {
                    emit(local)
                    return@collect
                }
                
                // Combine local results with API results
                try {
                    val apiIngredients = foodApiRepository.searchFoods(query)
                    // Filter out duplicates (match by name, case-insensitive)
                    val localNames = local.map { it.name.lowercase() }.toSet()
                    val uniqueApiIngredients = apiIngredients.filter { 
                        it.name.lowercase() !in localNames 
                    }
                    
                    // Emit combined results (local first, then API)
                    emit(local + uniqueApiIngredients)
                } catch (e: Exception) {
                    Log.e("MealRepositoryFirebase", "Error fetching API ingredients", e)
                    // If API fails, just emit local results
                    emit(local)
                }
            }
        }
    }

    suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredient(ingredient.toEntity())
    }

    suspend fun getIngredientById(id: Long): Ingredient? {
        return ingredientDao.getIngredientById(id)?.toDomain()
    }

    fun getUserIngredients(userId: String): Flow<List<Ingredient>> {
        return ingredientDao.getUserIngredients(userId).map { ingredientEntities ->
            ingredientEntities.map { it.toDomain() }
        }
    }

    suspend fun updateIngredient(ingredient: Ingredient) {
        ingredientDao.updateIngredient(ingredient.toEntity())
    }

    suspend fun deleteIngredient(ingredient: Ingredient) {
        ingredientDao.deleteIngredient(ingredient.toEntity())
    }
}

private const val COLLECTION_NAME_USERS = "users"
private const val DOCUMENT_FIELD_MEALS = "meals"
