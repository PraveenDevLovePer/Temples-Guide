package com.techdevlp.templesguide.ui.views.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.Black
import com.techdevlp.templesguide.ui.theme.Light_White
import com.techdevlp.templesguide.ui.views.home.TemplesData

@Composable
fun DetailsScreenComposable(navController: NavController, templeData: TemplesData?) {

    SetStatusBarColor(color = Color.Transparent, isIconLight = true)
    SetNavigationBarColor(color = Color.Transparent, isIconLight = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Light_White)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp40)))

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
                        Text(
                            text = templeData?.name ?: "",
                            style = TextStyle(
                                fontSize = spTextSizeResource(id = R.dimen.sp18),
                                color = Black
                            ),
                            modifier = Modifier.padding(
                                top = dimensionResource(id = R.dimen.dp10),
                                start = dimensionResource(id = R.dimen.dp10),
                                end = dimensionResource(id = R.dimen.dp10)
                            ),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = templeData?.story?:"",
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
                    }
                }
            }
        }
    }

}