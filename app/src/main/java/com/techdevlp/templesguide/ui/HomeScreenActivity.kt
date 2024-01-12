package com.techdevlp.templesguide.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.techdevlp.templesguide.ui.bottomnavigation.BottomNavNoAnimation
import com.techdevlp.templesguide.ui.bottomnavigation.Screen
import com.techdevlp.templesguide.ui.theme.TempleTripsTheme
import com.techdevlp.templesguide.ui.views.home.HomeScreenComposable

class HomeScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screen = listOf(
                Screen.Home,
                Screen.Create,
                Screen.Profile,
                Screen.Settings
            )

            TempleTripsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var loadScreen by remember { mutableIntStateOf(0) }
                    when (loadScreen) {
                        0 -> HomeScreenComposable(navController = rememberNavController())
                        1 -> HomeScreenComposable(navController = rememberNavController())
                        2 -> HomeScreenComposable(navController = rememberNavController())
                        3 -> HomeScreenComposable(navController = rememberNavController())
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BottomNavNoAnimation(
                            screens = screen,
                            onItemSelected = { selectedScreen ->
                                loadScreen = when (selectedScreen) {
                                    Screen.Home -> 0
                                    Screen.Create -> 1
                                    Screen.Profile -> 2
                                    Screen.Settings -> 3
                                }
                            }
                        )
                    }
                }

            }
        }
    }

}