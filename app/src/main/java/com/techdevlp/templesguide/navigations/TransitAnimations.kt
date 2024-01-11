package com.techdevlp.templesguide.navigations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun enterFadeTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(10))
}

fun exitFadeTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(10))
}

fun enterSlideTransition(): EnterTransition {
    return slideInHorizontally(initialOffsetX = { width -> width })
}

fun exitSlideTransition(): ExitTransition {
    return slideOutHorizontally(targetOffsetX = { width -> -width })
}

fun popEnterSlideTransition(): EnterTransition {
    return slideInHorizontally(initialOffsetX = { width -> -width })
}

fun popExitSlideTransition(): ExitTransition {
    return slideOutHorizontally(targetOffsetX = { width -> width })
}