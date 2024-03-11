package com.techdevlp.templesguide.navigations

import com.techdevlp.templesguide.Constants.TEMPLE_DETAILS_DATA


sealed class ScreenNames(val route: String) {
    data object SplashScreen : ScreenNames(route = "splash_screen")
    data object LoginScreen : ScreenNames(route = "login_screen")
    data object HomeScreen : ScreenNames(route = "home_screen")
    data object DetailsScreen : ScreenNames(route = "details_screen?$TEMPLE_DETAILS_DATA={$TEMPLE_DETAILS_DATA}"){
        fun passArguments(templeDetailsData:String?):String{
            return "details_screen?$TEMPLE_DETAILS_DATA=$templeDetailsData"
        }
    }
}