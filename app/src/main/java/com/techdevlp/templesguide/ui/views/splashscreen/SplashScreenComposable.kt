package com.techdevlp.templesguide.ui.views.splashscreen

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.ui.theme.AppThemeColor
import com.techdevlp.templesguide.ui.theme.Typography
import com.techdevlp.templesguide.ui.theme.White

@Composable
fun SplashScreenComposable(
    navController: NavController,
    mViewModel: SplashScreenViewModel = viewModel()
) {

    val activity = LocalContext.current as Activity
    SetAppLogo()
    CallGetCurrentLocation(navController = navController, mViewModel = mViewModel, context = activity)
}

@Composable
fun SetAppLogo(modifier: Modifier = Modifier) {
    val imageResource = painterResource(R.drawable.app_icon)
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
                Image(
                    painter = imageResource,
                    contentDescription = "Image Description",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                )
                Text(
                    text = MyApplicationContext.getContext().getString(R.string.app_name),
                    modifier = modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 1f),
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = White
                )
            }
            LinearProgressIndicator(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = White
            )
        }
    }
}

@Composable
fun CallGetCurrentLocation(navController: NavController, mViewModel: SplashScreenViewModel, context:Activity) {
    var isPermissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            isPermissionGranted = true
            mViewModel.getLocationAndGeocode(
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                    MyApplicationContext.getContext()
                ), navController = navController, context = context
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