package com.xectrone.quickmark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.xectrone.flashup.ui.theme.QuickMarkTheme
import com.xectrone.quickmark.domain.navigation.HomeScreenNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_QuickMark)
        setContent {
            QuickMarkTheme {
                HomeScreenNavGraph(navController = rememberNavController())
            }

        }
    }

}





