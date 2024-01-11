package com.techdevlp.templesguide

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetStatusBarColor(color: Color, isIconLight: Boolean) {
    val view: View = LocalView.current
    SideEffect {
        val window: Window = (view.context as Activity).window
        window.statusBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isIconLight
    }
}

@Composable
fun SetNavigationBarColor(color: Color, isIconLight: Boolean) {
    val view: View = LocalView.current
    SideEffect {
        val window: Window = (view.context as Activity).window
        window.navigationBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isIconLight
    }
}