package com.xectrone.quickmark.domain.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.ui.add_edit_note_screen.AddEditNoteScreen
import com.xectrone.quickmark.ui.home_screen.HomeScreen
import com.xectrone.quickmark.ui.settings_screen.SettingsScreen
import com.xectrone.quickmark.ui.theme.Constants.FILE_URI
import com.xectrone.quickmark.ui.theme.CustomAnimations

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreenNavGraph(navController: NavHostController)
{
    AnimatedNavHost(navController = navController, startDestination = Screen.Home.route )
    {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Setting.route,
            enterTransition = { CustomAnimations.slideInHorizontally() },
            exitTransition = { CustomAnimations.slideOutHorizontally() },
            popEnterTransition = { CustomAnimations.slideInHorizontally() },
            popExitTransition = { CustomAnimations.slideOutHorizontally() },
        ) {
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.AddEditNote.full,
            enterTransition = { CustomAnimations.slideInVertically() },
            exitTransition = { CustomAnimations.slideOutVertically() },
            popEnterTransition = { CustomAnimations.slideInVertically() },
            popExitTransition = { CustomAnimations.slideOutVertically() },
            arguments = listOf(
                navArgument(FILE_URI) {
                    type = NavType.StringType
                    defaultValue = "" }))
        {
            AddEditNoteScreen(fileUri = Util.decodeUri(it.arguments!!.getString(FILE_URI)!!),navController = navController)
        }
    }
}