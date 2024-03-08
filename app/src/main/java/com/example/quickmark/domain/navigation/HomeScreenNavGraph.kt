package com.example.quickmark.domain.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quickmark.ui.add_edit_note_screen.AddEditNoteScreen
import com.example.quickmark.ui.home_screen.HomeScreen
import com.example.quickmark.ui.settings_screen.SettingsScreen

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
        composable(route = Screen.AddEditNote.route){
            AddEditNoteScreen(navController = navController)
        }
    }
}