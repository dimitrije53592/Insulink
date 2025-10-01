package com.dj.insulink.core.navigation

sealed class Screen(val route: String) {
    object Registration : Screen("registration")
}