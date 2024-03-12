package com.techdevlp.templesguide.ui.views.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.Black
import com.techdevlp.templesguide.ui.theme.Light_White
import com.techdevlp.templesguide.ui.theme.Light_blue
import com.techdevlp.templesguide.ui.views.home.TemplesData

@Composable
fun DetailsScreenComposable(navController: NavController, templeData: TemplesData?) {

    SetStatusBarColor(color = Color.Transparent, isIconLight = true)
    SetNavigationBarColor(color = Color.Transparent, isIconLight = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(id = R.dimen.dp40),
                bottom = dimensionResource(id = R.dimen.dp20)
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
                                    .weight(1f), // Added weight modifier
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
                                    ) // Translate to English
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
                                    ) // Translate to Telugu
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
                                    ) // Translate to Tamil
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
                                    ) // Translate to Kannada
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
                                    ) // Translate to Hindi
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
        }
    }

}