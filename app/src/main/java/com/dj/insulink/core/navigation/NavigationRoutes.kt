package com.dj.insulink.core.navigation

sealed class Screen(val route: String) {
    object Registration : Screen("registration")
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object Home: Screen("home")
}