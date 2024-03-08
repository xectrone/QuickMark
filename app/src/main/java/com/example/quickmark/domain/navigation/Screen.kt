package com.example.quickmark.domain.navigation

sealed class Screen(val route: String) {
    object Home: Screen("HOME_SCREEN")
    object Setting: Screen("SETTING_SCREEN")
    object AddEditNote: Screen("ADD_EDIT_NOTE_SCREEN")
}