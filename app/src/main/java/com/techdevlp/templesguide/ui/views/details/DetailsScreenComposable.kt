package com.techdevlp.templesguide.ui.views.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.localdata.model.LocationDetails
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.AppThemeColor
import com.techdevlp.templesguide.ui.theme.Black
import com.techdevlp.templesguide.ui.theme.Light_White
import com.techdevlp.templesguide.ui.theme.Light_blue
import com.techdevlp.templesguide.ui.views.ads.BannerAd
import com.techdevlp.templesguide.ui.views.home.TemplesData
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun DetailsScreenComposable(templeData: TemplesData?) {
    val locationDetails = remember { mutableStateOf<LocationDetails?>(null) }
    val activity = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        //Getting location details saved in splash
        val location = LocalStoredData(MyApplicationContext.getContext()).getLocationDetails()
        locationDetails.value = location
    }

    SetStatusBarColor(color = Color.Transparent, isIconLight = true)
    SetNavigationBarColor(color = Color.Transparent, isIconLight = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(id = R.dimen.dp40),
                bottom = dimensionResource(id = R.dimen.dp30)
            )
            .background(Light_White)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                AsyncImage(
                    model = templeData?.imageUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color.Gray)
                        .fillMaxWidth(),
                    placeholder = painterResource(id = R.drawable.app_icon)
                )

                NameAndStoryViewsUI(templeData = templeData)

                MapViewUI(
                    templeData = templeData,
                    context = activity,
                    locationDetails = locationDetails
                )

                TimingViewsUI(templeData = templeData)

                AddressViewsUI(templeData = templeData)
                
                BannerAd()
            }
        }
    }
}

/**
 * Showing Name and Address module ui.
 * @Version V1.0
 */
@Composable
fun NameAndStoryViewsUI(templeData: TemplesData?) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.dp16))
            .fillMaxWidth(),
        elevation = dimensionResource(id = R.dimen.dp4),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp16)),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dp10))
                .fillMaxWidth()
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf("English") }
            var storyBasedOnLanguage by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = Unit) {
                storyBasedOnLanguage = templeData?.story ?: ""
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = templeData?.name ?: "",
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp18),
                        color = Black
                    ),
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.dp10),
                            start = dimensionResource(id = R.dimen.dp10),
                            end = dimensionResource(id = R.dimen.dp10)
                        )
                        .weight(1f),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.dp10))
                        .clickable {
                            expanded = !expanded
                        }
                        .background(
                            color = Light_blue, shape = RoundedCornerShape(
                                dimensionResource(id = R.dimen.dp8)
                            )
                        )
                )

                fun changeLanguageUsingML(text: String, targetLanguage: Int) {
                    val options = FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(targetLanguage)
                        .build()
                    val englishTeluguTranslator =
                        FirebaseNaturalLanguage.getInstance().getTranslator(options)
                    englishTeluguTranslator.downloadModelIfNeeded()
                        .addOnSuccessListener {
                            englishTeluguTranslator.translate(text)
                                .addOnSuccessListener { translatedText ->
                                    isLoading = false
                                    storyBasedOnLanguage = translatedText
                                }
                                .addOnFailureListener { exception ->
                                    isLoading = false
                                }
                        }
                        .addOnFailureListener { exception ->
                            isLoading = false
                        }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        isLoading = true
                        selectedOption = "English"
                        expanded = false
                        changeLanguageUsingML(
                            templeData?.story ?: "",
                            FirebaseTranslateLanguage.EN
                        )
                    }) {
                        Text("English")
                    }
                    DropdownMenuItem(onClick = {
                        isLoading = true
                        selectedOption = "Telugu"
                        expanded = false
                        changeLanguageUsingML(
                            templeData?.story ?: "",
                            FirebaseTranslateLanguage.TE
                        )
                    }) {
                        Text("Telugu")
                    }
                    DropdownMenuItem(onClick = {
                        isLoading = true
                        selectedOption = "Tamil"
                        expanded = false
                        changeLanguageUsingML(
                            templeData?.story ?: "",
                            FirebaseTranslateLanguage.TA
                        )
                    }) {
                        Text("Tamil")
                    }
                    DropdownMenuItem(onClick = {
                        isLoading = true
                        selectedOption = "Kannada"
                        expanded = false
                        changeLanguageUsingML(
                            templeData?.story ?: "",
                            FirebaseTranslateLanguage.KN
                        )
                    }) {
                        Text("Kannada")
                    }
                    DropdownMenuItem(onClick = {
                        isLoading = true
                        selectedOption = "Hindi"
                        expanded = false
                        changeLanguageUsingML(
                            templeData?.story ?: "",
                            FirebaseTranslateLanguage.HI
                        )
                    }) {
                        Text("Hindi")
                    }
                }

            }

            if (!isLoading) {
                Text(
                    text = storyBasedOnLanguage ?: templeData?.story ?: "",
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp16),
                        color = Black
                    ),
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.dp10),
                        start = dimensionResource(id = R.dimen.dp10),
                        end = dimensionResource(id = R.dimen.dp10)
                    )
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.dp50))
                        .align(Alignment.CenterHorizontally),
                    color = Light_blue
                )
            }
        }
    }
}

/**
 * Showing Maps module ui.
 * @Version V1.0
 */
@Composable
fun MapViewUI(
    templeData: TemplesData?,
    context: Context,
    locationDetails: MutableState<LocationDetails?>
) {
    Text(
        text = "Explore the area",
        style = TextStyle(
            fontSize = spTextSizeResource(id = R.dimen.sp17),
            color = Black
        ),
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.dp10),
                start = dimensionResource(id = R.dimen.dp16),
                end = dimensionResource(id = R.dimen.dp16)
            ),
        fontWeight = FontWeight.Bold
    )

    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.dp16))
            .fillMaxWidth(),
        elevation = dimensionResource(id = R.dimen.dp4),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp16)),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Configuration.getInstance()
                .load(context, context.getSharedPreferences("OpenStreetMap", Context.MODE_PRIVATE))

            val mapView = MapView(context)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)

            val controller: IMapController = mapView.controller
            controller.setZoom(16.5)
            val startPoint = GeoPoint(
                templeData?.latitude?.toDouble() ?: 0.00,
                templeData?.longitude?.toDouble() ?: 0.00
            )
            controller.setCenter(startPoint)

            val marker = org.osmdroid.views.overlay.Marker(mapView)
            marker.position = startPoint
            mapView.overlays.add(marker)

            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.dp180)),
                factory = { mapView }
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = Light_blue)
                .padding(dimensionResource(id = R.dimen.dp10))
                .clickable {
                    openMapsAppWithDirections(
                        context = context,
                        startLat = locationDetails.value?.currentLat ?: 0.00,
                        startLong = locationDetails.value?.currentLng ?: 0.00,
                        endLat = templeData?.latitude?.toDouble() ?: 0.00,
                        endLong = templeData?.longitude?.toDouble() ?: 0.00
                    )
                }) {
                Text(
                    text = "Navigate",
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp16),
                        color = AppThemeColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "",
                    tint = AppThemeColor
                )
            }
        }
    }
}

/**
 * Showing Address module ui.
 * @Version V1.0
 */
@Composable
fun AddressViewsUI(templeData: TemplesData?) {
    val dialerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        }

    Text(
        text = "Address:",
        style = TextStyle(
            fontSize = spTextSizeResource(id = R.dimen.sp16),
            color = Black
        ),
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.dp10),
                start = dimensionResource(id = R.dimen.dp16),
                end = dimensionResource(id = R.dimen.dp16)
            ),
        fontWeight = FontWeight.Bold
    )

    Text(
        text = templeData?.address ?: "",
        style = TextStyle(
            fontSize = spTextSizeResource(id = R.dimen.sp14),
            color = Color.Gray
        ),
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.dp20),
                end = dimensionResource(id = R.dimen.dp20)
            )
    )

    Text(
        text = templeData?.city ?: "",
        style = TextStyle(
            fontSize = spTextSizeResource(id = R.dimen.sp14),
            color = Color.Gray
        ),
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.dp20),
                end = dimensionResource(id = R.dimen.dp20)
            )
    )

    Text(
        text = templeData?.state ?: "",
        style = TextStyle(
            fontSize = spTextSizeResource(id = R.dimen.sp14),
            color = Color.Gray
        ),
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.dp20),
                end = dimensionResource(id = R.dimen.dp20)
            )
    )

    if (templeData?.helpLine?.isNotEmpty() == true) {
        Row(
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.dp20),
                    end = dimensionResource(id = R.dimen.dp20)
                )
        ) {
            Text(
                text = "Help Line: ",
                style = TextStyle(
                    fontSize = spTextSizeResource(id = R.dimen.sp14),
                    color = Color.Gray
                )
            )

            Text(
                text = templeData.helpLine,
                style = TextStyle(
                    fontSize = spTextSizeResource(id = R.dimen.sp14),
                    color = AppThemeColor
                ),
                modifier = Modifier
                    .clickable {
                        val phoneNumber = templeData.helpLine
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$phoneNumber")
                        dialerLauncher.launch(intent)
                    }
            )
        }
    }
    Spacer(modifier = Modifier.run { height(dimensionResource(id = R.dimen.dp16)) })
}

/**
 * Showing Address module ui.
 * @Version V1.0
 */
@Composable
fun TimingViewsUI(templeData: TemplesData?) {
    Text(
        text = "Timings:",
        style = TextStyle(
            fontSize = spTextSizeResource(id = R.dimen.sp17),
            color = Black
        ),
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.dp10),
                start = dimensionResource(id = R.dimen.dp16),
                end = dimensionResource(id = R.dimen.dp16)
            ),
        fontWeight = FontWeight.Bold
    )

    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.dp16))
            .fillMaxWidth(),
        elevation = dimensionResource(id = R.dimen.dp4),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp16)),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.dp10))) {
            Text(
                text = "Daily",
                style = TextStyle(
                    fontSize = spTextSizeResource(id = R.dimen.sp16),
                    color = Black
                ),
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.dp8),
                        start = dimensionResource(id = R.dimen.dp8),
                        end = dimensionResource(id = R.dimen.dp8)
                    ),
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.dp8))
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Open: ${templeData?.open}",
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp16),
                        color = Black
                    ),
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.dp8))
                        .weight(1f),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Close: ${templeData?.close}",
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp16),
                        color = Black
                    ),
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.dp8))
                        .weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

/**
 * Open google maps functionality.
 * @Version V1.0
 */
fun openMapsAppWithDirections(
    context: Context,
    startLat: Double,
    startLong: Double,
    endLat: Double,
    endLong: Double
) {
    val uri = "http://maps.google.com/maps?saddr=$startLat,$startLong&daddr=$endLat,$endLong"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}