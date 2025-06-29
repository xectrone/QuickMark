package com.xectrone.quickmark.ui.theme


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally



object CustomAnimations {
    fun slideInHorizontally(): EnterTransition {
        return fadeIn(animationSpec = tween(200, easing = EaseInOut)) +
                slideInHorizontally(
                    initialOffsetX = {200},
                    animationSpec = tween(200, easing = EaseInOut)
                )
    }
    fun slideOutHorizontally(): ExitTransition {
        return fadeOut(animationSpec = tween(200, easing = EaseInOut)) +
                slideOutHorizontally(
                    targetOffsetX = {200},
                    animationSpec = tween(200, easing = EaseInOut)
                )
    }

    fun slideInVertically(): EnterTransition {
        return fadeIn(animationSpec = tween(200, easing = EaseInOut)) +
                androidx.compose.animation.slideInVertically(
                    initialOffsetY = {200},
                    animationSpec = tween(200, easing = EaseInOut)
                )
    }

    fun slideOutVertically(): ExitTransition {
        return fadeOut(animationSpec = tween(200, easing = EaseInOut)) +
                androidx.compose.animation.slideOutVertically (
                    targetOffsetY = {200},
                    animationSpec = tween(200, easing = EaseInOut)
                )
    }

    fun expand(): EnterTransition {
        return fadeIn(animationSpec = tween(200, easing = EaseInOut)) +
                expandIn(
                    animationSpec = tween(200, easing = EaseInOut)
                )
    }

    fun shrink(): ExitTransition {
        return fadeOut(animationSpec = tween(200, easing = EaseInOut)) +
                shrinkOut   (
                    animationSpec = tween(200, easing = EaseInOut)
                )
    }





}