package com.techdevlp.templesguide.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.ui.views.home.HomeScreenComposable
import com.techdevlp.templesguide.ui.views.login.LoginScreenComposable
import com.techdevlp.templesguide.ui.views.splashscreen.SplashScreenComposable

@Composable
fun NavGraphComposable(navController: NavHostController) {
    SetStatusBarColor(
        color = Color.Transparent,
        isIconLight = false
    )
    SetNavigationBarColor(
        color = Color.Transparent,
        isIconLight = false
    )

    NavHost(
        navController = navController,
        startDestination = ScreenNames.SplashScreen.route,
        enterTransition = { enterFadeTransition() },
        exitTransition = { exitFadeTransition() },
        popEnterTransition = { popEnterSlideTransition() },
        popExitTransition = { popExitSlideTransition() }
    ) {
        composable(route = ScreenNames.SplashScreen.route) {
            SplashScreenComposable(navController = navController)
        }

        composable(route = ScreenNames.LoginScreen.route) {
            LoginScreenComposable(navController = navController)
        }

        composable(route = ScreenNames.HomeScreen.route) {
            HomeScreenComposable(navController = navController)
        }
    }
}