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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.LocationDetails
import com.techdevlp.templesguide.localdata.model.UserDetails
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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = userDetails.value?.fullName ?: "Hello",
            style = TextStyle(fontSize = 20.sp, color = Black),
            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
        )

        Row {
            Icon(
                imageVector = Icons.Outlined.LocationOn, contentDescription = "Location Icon",
                tint = Color.Gray, modifier = Modifier
                    .padding(start = 15.dp, top = 7.dp)
                    .size(15.dp)
            )

            Text(
                text = locationDetails.value?.city ?: "",
                style = TextStyle(fontSize = 13.sp, color = Color.Gray),
                modifier = Modifier.padding(top = 6.dp, start = 5.dp)
            )
        }

        val (value, onValueChange) = remember { mutableStateOf("") }
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Black),
            leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.Gray) },
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .background(Color(0xFFFFF3EA), RoundedCornerShape(50.dp)),
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
    HomeScreenComposable(navController = rememberNavController())
}