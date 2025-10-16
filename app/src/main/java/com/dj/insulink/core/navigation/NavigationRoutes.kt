package com.dj.insulink.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
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
    object Fitness : Screen("fitness", icon = Icons.AutoMirrored.Filled.DirectionsRun, title = "Fitness")
    object Reminders : Screen("reminders", title = "Reminders")
    object Friends : Screen("friends", title = "Friends")
    object Reports : Screen("reports", title = "Reports")

    companion object {
        val allDestinations: List<Screen> = listOf(
            Registration,
            Login,
            ForgotPassword,
            Glucose,
            Meals,
            Fitness,
            Reminders,
            Friends,
            Reports
        )

        val bottomBarDestinations: List<Screen> = listOf(
            Meals,
            Glucose,
            Fitness
        )

        val topBarAndSideDrawerDestinations: List<Screen> = bottomBarDestinations + listOf(
            Reminders,
            Friends,
            Reports
        )

        fun findDestinationByRoute(route: String?): Screen? {
            return allDestinations.find { it.route == route }
        }
    }
}