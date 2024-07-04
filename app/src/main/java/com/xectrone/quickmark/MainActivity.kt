package com.xectrone.quickmark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.xectrone.flashup.ui.theme.QuickMarkTheme
import com.xectrone.quickmark.domain.navigation.HomeScreenNavGraph


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_QuickMark)
        setContent {
            QuickMarkTheme {
                HomeScreenNavGraph(navController = rememberAnimatedNavController())
            }

        }
    }

}





