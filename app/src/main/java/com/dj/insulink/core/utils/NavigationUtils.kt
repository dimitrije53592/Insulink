package com.dj.insulink.core.utils

import androidx.navigation.NavHostController

fun NavHostController.navigateTo(route: String) {
   this.navigate(route) {
       launchSingleTop = true
   }
}