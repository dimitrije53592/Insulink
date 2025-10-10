package com.dj.insulink.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector? = null,
    val title: String = ""
) {
    object Registration : Screen("registration")
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object Glucose : Screen("glucose", icon = Icons.Filled.WaterDrop, title = "Glucose")
    object Meals : Screen("meals", icon = Icons.Filled.Restaurant, title = "Meals")
    object Fitness : Screen("fitness", icon = Icons.Filled.DirectionsRun, title = "Fitness")
}

val bottomBarDestinations: List<Screen> = listOf(
    Screen.Meals,
    Screen.Glucose,
    Screen.Fitness
)