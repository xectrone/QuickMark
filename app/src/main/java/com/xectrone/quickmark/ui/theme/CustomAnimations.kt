package com.xectrone.quickmark.ui.theme


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally



object CustomAnimations {
    fun slideInHorizontally(): EnterTransition {
        return fadeIn(animationSpec = tween(300)) +
                slideInHorizontally(
                    initialOffsetX = {300},
                    animationSpec = tween(300)
                )
    }
    fun slideOutHorizontally(): ExitTransition {
        return fadeOut(animationSpec = tween(300)) +
                slideOutHorizontally(
                    targetOffsetX = {300},
                    animationSpec = tween(300)
                )
    }

    fun slideInVertically(): EnterTransition {
        return fadeIn(animationSpec = tween(300)) +
                androidx.compose.animation.slideInVertically(
                    initialOffsetY = {300},
                    animationSpec = tween(300)
                )
    }

    fun slideOutVertically(): ExitTransition {
        return fadeOut(animationSpec = tween(300)) +
                androidx.compose.animation.slideOutVertically (
                    targetOffsetY = {300},
                    animationSpec = tween(300)
                )
    }

    fun expand(): EnterTransition {
        return fadeIn(animationSpec = tween(300)) +
                expandIn(
                    animationSpec = tween(300)
                )
    }

    fun shrink(): ExitTransition {
        return fadeOut(animationSpec = tween(300)) +
                shrinkOut   (
                    animationSpec = tween(300)
                )
    }





}