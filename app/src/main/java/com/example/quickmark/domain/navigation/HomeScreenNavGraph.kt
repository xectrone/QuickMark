package com.example.quickmark.domain.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quickmark.domain.Util
import com.example.quickmark.ui.add_edit_note_screen.AddEditNoteScreen
import com.example.quickmark.ui.home_screen.HomeScreen
import com.example.quickmark.ui.settings_screen.SettingsScreen
import com.example.quickmark.ui.theme.Constants.FILE_URI

@Composable
fun HomeScreenNavGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = Screen.Home.route )
    {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Setting.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.AddEditNote.full,
            arguments = listOf(
                navArgument(FILE_URI) {
                    type = NavType.StringType
                    defaultValue = "" }))
        {
            AddEditNoteScreen(fileUri = Util.decodeUri(it.arguments!!.getString(FILE_URI)!!),navController = navController)
        }
    }
}