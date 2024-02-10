package com.techdevlp.templesguide.ui.views.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.LocationDetails
import com.techdevlp.templesguide.localdata.model.UserDetails
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.Black

@Composable
fun HomeScreenComposable(navController: NavController) {
    val userDetails = remember { mutableStateOf<UserDetails?>(null) }
    val locationDetails = remember { mutableStateOf<LocationDetails?>(null) }

    LaunchedEffect(Unit) { // Launch a coroutine to fetch user details
        val details = LocalStoredData(MyApplicationContext.getContext()).getUserDetails()
        userDetails.value = details

        val location = LocalStoredData(MyApplicationContext.getContext()).getLocationDetails()
        locationDetails.value = location
    }
    SetStatusBarColor(color = Color.Transparent, isIconLight = true)
    SetNavigationBarColor(color = Color.Transparent, isIconLight = true)

    HeaderViewsUi(userDetails, locationDetails)

    HomeListUI()
}

@Composable
fun HomeListUI() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Text(text = "Suggested temples", style = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp18)))
    }
}

@Composable
fun HeaderViewsUi(
    userDetails: MutableState<UserDetails?>,
    locationDetails: MutableState<LocationDetails?>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp40)))
        Text(
            text = userDetails.value?.fullName ?: "Hello",
            style = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp20), color = Black),
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dp15), end = dimensionResource(id = R.dimen.dp15))
        )

        Row {
            Icon(
                imageVector = Icons.Outlined.LocationOn, contentDescription = "Location Icon",
                tint = Color.Gray, modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.dp15),
                        top = dimensionResource(id = R.dimen.dp7)
                    )
                    .size(dimensionResource(id = R.dimen.dp15))
            )

            Text(
                text = locationDetails.value?.city ?: "",
                style = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp13), color = Color.Gray),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp6), start = dimensionResource(id = R.dimen.dp5))
            )
        }

        val (value, onValueChange) = remember { mutableStateOf("") }
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp16), color = Black),
            leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.Gray) },
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dp15))
                .fillMaxWidth()
                .background(Color(0xFFFFF3EA), RoundedCornerShape(dimensionResource(id = R.dimen.dp50))),
            placeholder = { Text(text = "Search places here") },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Gray,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Color.Black
            )
        )
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeListUI()
}