package com.techdevlp.templesguide.ui.views.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.techdevlp.templesguide.MyApplicationContext
import com.techdevlp.templesguide.R
import com.techdevlp.templesguide.SetNavigationBarColor
import com.techdevlp.templesguide.SetStatusBarColor
import com.techdevlp.templesguide.localdata.LocalStoredData
import com.techdevlp.templesguide.navigations.ScreenNames
import com.techdevlp.templesguide.spTextSizeResource
import com.techdevlp.templesguide.ui.theme.AppThemeColor
import com.techdevlp.templesguide.ui.theme.Light_blue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingComposable(
    navController: NavController
) {
    SetStatusBarColor(color = Color.Transparent, isIconLight = true)
    SetNavigationBarColor(color = Color.Transparent, isIconLight = true)

    val itemsList: List<OnBoardingItems> = OnBoardingItems.getData()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val pagerState: PagerState = rememberPagerState(initialPage = 0, pageCount = { itemsList.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Light_blue)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp40)))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.dp30)),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) { page ->
            val scale: Float by animateFloatAsState(
                targetValue = if (page == pagerState.currentPage) 1f else 0.9f,
                animationSpec = tween(durationMillis = 300),
                label = "Label"
            )
            OnBoardingItemsComposable(
                item = itemsList[page],
                scale = scale,
                pageOffset = page.toFloat() - pagerState.currentPage.toFloat()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp20)))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box {
                Indicators(size = itemsList.size, index = pagerState.currentPage)
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp20)))

        androidx.compose.material3.Button(
            onClick = {
                coroutineScope.launch {
                    val dataStore = LocalStoredData(MyApplicationContext.getContext())
                    dataStore.setOnBoardStatus(true)
                    navController.navigate(route = ScreenNames.LoginScreen.route) {
                        popUpTo(route = ScreenNames.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.dp20),
                    end = dimensionResource(id = R.dimen.dp20)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = AppThemeColor)
        ) {
            Text("Start Explore", color = Color.White, fontSize = spTextSizeResource(id = R.dimen.sp16))
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp30)))
    }
}

/**
 * Item Views UI.
 * @Version V1.0
 */
@Composable
fun OnBoardingItemsComposable(
    item: OnBoardingItems,
    scale: Float,
    pageOffset: Float
) {
    val colorMatrix = remember { ColorMatrix() }
    LaunchedEffect(key1 = scale) {
        if (pageOffset != 0.0f) {
            colorMatrix.setToSaturation(0.5f)
        } else {
            colorMatrix.setToSaturation(1f)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp16))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp20)))
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f),
                    colorFilter = ColorFilter.colorMatrix(colorMatrix)
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp20)))
                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp18),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.dp30),
                            end = dimensionResource(id = R.dimen.dp30)
                        ),
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp20)))
                Text(
                    text = item.desc,
                    style = TextStyle(
                        fontSize = spTextSizeResource(id = R.dimen.sp15),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.dp30),
                            end = dimensionResource(id = R.dimen.dp30)
                        ),
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp30)))
            }
        }
    }
}

/**
 * Indicators functionality.
 * @Version V1.0
 */
@Composable
fun BoxScope.Indicators(
    size: Int,
    index: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp5)),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

/**
 * Indicator ui.
 * @Version V1.0
 */
@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) dimensionResource(id = R.dimen.dp8) else dimensionResource(id = R.dimen.dp8),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )

    Box(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.dp8))
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) AppThemeColor else Color.Gray
            )
    )
}

/**
 * Onboarding items model.
 * @Version V1.0
 */
class OnBoardingItems(
    val image: Int,
    val title: String,
    val desc: String
) {
    companion object {
        fun getData(): List<OnBoardingItems> {
            return listOf(
                OnBoardingItems(
                    R.drawable.onboard_one,
                    "Welcome to TemplesGuide!",
                    "Discover temples near Tirupati city and immerse yourself in their stories. TemplesGuide is your gateway to exploring the rich cultural heritage of the region."
                ),
                OnBoardingItems(
                    R.drawable.onboard_two,
                    "Explore in Your Language",
                    "TemplesGuide offers temple stories and information in multiple languages including English, Telugu, Tamil, Kannada, and Hindi. Experience the richness of temple narratives in a language that resonates with you."
                ),
                OnBoardingItems(
                    R.drawable.onboard_three,
                    "Find Your Way",
                    "With TemplesGuide, locating temples is effortless. Use our integrated maps feature to pinpoint temple locations and navigate with ease. Discover the hidden gems of Tirupati city with just a tap."
                ),
                OnBoardingItems(
                    R.drawable.onboard_four,
                    "Your Privacy, Our Priority",
                    "We respect your privacy. TemplesGuide does not read or store your personal data. Enjoy exploring temples near Tirupati city without worrying about your privacy. Your journey with us is secure and confidential."
                )
            )
        }
    }
}