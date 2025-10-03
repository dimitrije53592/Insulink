package com.dj.insulink.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dj.insulink.login.ui.screens.LoginScreen
import com.dj.insulink.registration.ui.screens.RegistrationScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Registration.route) {
            RegistrationScreen()
        }
        composable(Screen.Login.route) {
            LoginScreen()
        }
    }
}