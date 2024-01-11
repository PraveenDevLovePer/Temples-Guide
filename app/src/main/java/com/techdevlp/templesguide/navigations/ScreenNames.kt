package com.techdevlp.templesguide.navigations

sealed class ScreenNames(val route: String) {
    data object SplashScreen : ScreenNames(route = "splash_screen")
    data object LoginScreen : ScreenNames(route = "login_screen")
    data object HomeScreen : ScreenNames(route = "home_screen")
}