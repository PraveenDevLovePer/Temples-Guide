package com.techdevlp.templesguide.ui.views.splashscreen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.LocationDetails
import com.techdevlp.templesguide.navigations.ScreenNames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class SplashScreenViewModel : ViewModel() {

    /**
     * Get device location and address info using Geocode.
     * @Version V1.0
     */
    fun getLocationAndGeocode(
        fusedLocationClient: FusedLocationProviderClient,
        navController: NavController
    ) {
        viewModelScope.launch {
            try {
                if (ActivityCompat.checkSelfPermission(
                        MyApplicationContext.getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        MyApplicationContext.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    navigateToNextScreen(navController = navController)
                    return@launch
                }

                val location = withContext(Dispatchers.IO) {
                    fusedLocationClient.lastLocation.await()
                }

                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val geocoder =
                        Geocoder(MyApplicationContext.getContext(), Locale.getDefault())
                    val addresses = withContext(Dispatchers.IO) {
                        geocoder.getFromLocation(latitude, longitude, 1)
                    }

                    if (addresses?.isNotEmpty() == true) {
                        val address = addresses[0]
                        val fullAddress = address.getAddressLine(0)
                        val city = address.locality
                        val zipcode = address.postalCode
                        val state = address.adminArea
                        val country = address.countryName

                        val locationDetails = LocationDetails(
                            address = fullAddress,
                            city = city,
                            zipCode = zipcode,
                            state = state,
                            country = country,
                            currentLat = latitude,
                            currentLng = longitude
                        )
                        val dataStore = LocalStoredData(MyApplicationContext.getContext())
                        dataStore.setLocationDetails(locationDetails)
                        navigateToNextScreen(navController = navController)
                    } else {
                        navigateToNextScreen(navController = navController)
                    }
                } else {
                    navigateToNextScreen(navController = navController)
                }
            } catch (e: Exception) {
                navigateToNextScreen(navController = navController)
            }
        }
    }

    /**
     * Navigate to another screen after fetch location details.
     * @Version V1.0
     */
    private fun navigateToNextScreen(navController: NavController) {
        viewModelScope.launch {
            val dataStore = LocalStoredData(MyApplicationContext.getContext())
            delay(3000)
            if (dataStore.getOnBoardStatus() != true){
                navController.navigate(route = ScreenNames.OnBoardingScreen.route) {
                    popUpTo(route = ScreenNames.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }else if (dataStore.getUserDetails()?.userId == "" || dataStore.getUserDetails()?.userId.isNullOrEmpty()) {
                navController.navigate(route = ScreenNames.LoginScreen.route) {
                    popUpTo(route = ScreenNames.SplashScreen.route) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(route = ScreenNames.HomeScreen.route) {
                    popUpTo(route = ScreenNames.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}