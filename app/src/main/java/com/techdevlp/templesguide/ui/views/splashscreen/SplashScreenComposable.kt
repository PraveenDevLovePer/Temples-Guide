package com.techdevlp.templesguide.ui.views.splashscreen

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.ui.theme.AppThemeColor
import com.techdevlp.templesguide.ui.theme.Typography

@Composable
fun SplashScreenComposable(
    navController: NavController,
    mViewModel: SplashScreenViewModel = viewModel()
) {

    val activity = LocalContext.current as Activity
    SetAppLogo()
    CallGetCurrentLocation(
        navController = navController,
        mViewModel = mViewModel,
        context = activity
    )
}

/**
 * Splash screen ui
 * @Version V1.0
 */
@Composable
fun SetAppLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppThemeColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = MyApplicationContext.getContext().getString(R.string.app_name),
                    modifier = modifier
                        .fillMaxWidth(),
                    style = Typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
            }
        }
    }
}

/**
 * Ask location permission and Fetch device location.
 * @Version V1.0
 */
@Composable
fun CallGetCurrentLocation(
    navController: NavController,
    mViewModel: SplashScreenViewModel,
    context: Activity
) {
    var isPermissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            isPermissionGranted = true
            mViewModel.getLocationAndGeocode(
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                    MyApplicationContext.getContext()
                ), navController = navController
            )
        } else {
            isPermissionGranted = false
        }
    }

    if (!isPermissionGranted) {
        LaunchedEffect(key1 = true) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}