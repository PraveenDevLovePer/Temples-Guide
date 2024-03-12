package com.techdevlp.templesguide.ui.views.home

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.LocationDetails
import com.techdevlp.templesguide.localdata.model.UserDetails
import com.techdevlp.templesguide.navigations.ScreenNames
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.Black
import com.techdevlp.templesguide.ui.theme.Light_blue

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreenComposable(
    navController: NavController,
    myViewModel: HomeScreenViewModel = viewModel()
) {
    val userDetails = remember { mutableStateOf<UserDetails?>(null) }
    val locationDetails = remember { mutableStateOf<LocationDetails?>(null) }
    val activity = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        //Getting user details saved in login
        val details = LocalStoredData(MyApplicationContext.getContext()).getUserDetails()
        userDetails.value = details

        //Getting location details saved in splash
        val location = LocalStoredData(MyApplicationContext.getContext()).getLocationDetails()
        locationDetails.value = location

//        Initialise the fire base
        FirebaseApp.initializeApp(activity)
        myViewModel.getTemples()
    }

    SetStatusBarColor(color = Color.Transparent, isIconLight = true)
    SetNavigationBarColor(color = Color.Transparent, isIconLight = true)

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderViewsUi(userDetails, locationDetails)
        TemplesListUI(myViewModel = myViewModel, activity = activity, navController = navController)
    }
}

/**
 * HomeScreen top View ui and functionality.
 * @Version V1.0
 */
@Composable
fun HeaderViewsUi(
    userDetails: MutableState<UserDetails?>,
    locationDetails: MutableState<LocationDetails?>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp40)))
        Text(
            text = userDetails.value?.fullName ?: "Hello",
            style = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp20), color = Black),
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.dp16),
                end = dimensionResource(id = R.dimen.dp16)
            )
        )

        Row {
            Icon(
                imageVector = Icons.Outlined.LocationOn, contentDescription = "Location Icon",
                tint = Color.Gray, modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.dp16),
                        top = dimensionResource(id = R.dimen.dp7)
                    )
                    .size(dimensionResource(id = R.dimen.dp15))
            )

            Text(
                text = locationDetails.value?.city ?: "",
                style = TextStyle(
                    fontSize = spTextSizeResource(id = R.dimen.sp13),
                    color = Color.Gray
                ),
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.dp6),
                    start = dimensionResource(id = R.dimen.dp5),
                    end = dimensionResource(id = R.dimen.dp16)
                )
            )
        }

        val (value, onValueChange) = remember { mutableStateOf("") }
        TextField(
            value = value,
            onValueChange = { newValue ->
                val filteredValue = newValue.filter { it.isLetter() && it in 'a'..'z' }
                onValueChange(filteredValue)
            },
            textStyle = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp16), color = Black),
            leadingIcon = {
                Icon(Icons.Filled.Search, null, tint = Color.Gray)
            },
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.dp15),
                    end = dimensionResource(id = R.dimen.dp15),
                    top = dimensionResource(id = R.dimen.dp10)
                )
                .height(dimensionResource(id = R.dimen.dp53))
                .fillMaxWidth()
                .background(
                    Light_blue,
                    RoundedCornerShape(dimensionResource(id = R.dimen.dp50))
                ),
            placeholder = { Text(text = "eg: tirupati") },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Gray,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            singleLine = true
        )
    }
}

/**
 * HomeScreen body ui and functionality.
 * @Version V1.0
 */
@Composable
fun TemplesListUI(
    myViewModel: HomeScreenViewModel,
    activity: Activity,
    navController: NavController
) {
    val templeList by myViewModel.templeList.observeAsState(emptyList())
    val listState = rememberLazyGridState()

    Text(
        text = "Popular visits",
        style = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp18), color = Black),
        modifier = Modifier.padding(
            top = dimensionResource(id = R.dimen.dp15),
            start = dimensionResource(id = R.dimen.dp15),
            end = dimensionResource(id = R.dimen.dp15),
            bottom = dimensionResource(id = R.dimen.dp10)
        ),
        fontWeight = FontWeight.Bold
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.dp10),
                end = dimensionResource(id = R.dimen.dp10)
            )
    ) {

        items(templeList.size) { index ->
            val templeData = templeList[index]
            ListItemsUI(templeData = templeData, activity = activity) {
                navController.navigate(
                    route = ScreenNames.DetailsScreen.passArguments(
                        templeDetailsData = Gson().toJson(templeData)
                    )
                )
            }
        }
    }
}

/**
 * List item ui and functionality.
 * @Version V1.0
 */
@Composable
fun ListItemsUI(templeData: TemplesData?, activity: Activity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.dp7))
            .background(
                Light_blue,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp25))
            )
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = templeData?.imageUrl,
            contentDescription = "",
            imageLoader = ImageLoader(activity),
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dp25)))
                .background(Color.Gray)
                .fillMaxSize(),
            placeholder = painterResource(id = R.drawable.app_icon)
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp10)))

        Text(
            text = templeData?.name ?: "",
            style = TextStyle(fontSize = spTextSizeResource(id = R.dimen.sp16), color = Black),
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.dp10),
                end = dimensionResource(id = R.dimen.dp10)
            ),
            maxLines = 1
        )

        Row {
            Icon(
                imageVector = Icons.Outlined.LocationOn, contentDescription = "Location Icon",
                tint = Color.Gray, modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.dp10),
                        top = dimensionResource(id = R.dimen.dp7)
                    )
                    .size(dimensionResource(id = R.dimen.dp15))
            )

            Text(
                text = templeData?.city ?: "",
                style = TextStyle(
                    fontSize = spTextSizeResource(id = R.dimen.sp13),
                    color = Color.Gray
                ),
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.dp6),
                    start = dimensionResource(id = R.dimen.dp5),
                    end = dimensionResource(id = R.dimen.dp10)
                ),
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp10)))

    }
}

/**
 * Model for TempleData.
 * @Version V1.0
 */
data class TemplesData(
    val name: String,
    val city: String,
    val imageUrl: String,
    val address: String,
    val state: String,
    val open: String,
    val close: String,
    val latitude: String,
    val longitude: String,
    val story: String,
    val helpLine: String
)