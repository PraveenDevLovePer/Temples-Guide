package com.techdevlp.templesguide

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.TypedValue
import android.view.View
import android.view.Window
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
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

@Composable
fun spTextSizeResource(@DimenRes id: Int): TextUnit {
    val resources: Resources = LocalContext.current.resources

    return with(TypedValue()) {
        resources.getValue(id, this, true)
        if (type == TypedValue.TYPE_DIMENSION && complexUnit == TypedValue.COMPLEX_UNIT_SP) {
            val scaledValue = TypedValue.complexToFloat(data)
            scaledValue.sp
        } else {
            error("Dimension resource $id is not of type COMPLEX_UNIT_SP.")
        }
    }
}

fun dpToPx(context : Context, dpValue : Float) : Float {
    return dpValue * context.resources.displayMetrics.density
}

fun pxToDp(context: Context, pxValue: Float): Float {
    return pxValue / context.resources.displayMetrics.density
}

fun isInternetAvailable(context: Context): Boolean {
    val result: Boolean
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}