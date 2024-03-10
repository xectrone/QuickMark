package com.example.quickmark.domain.navigation

import com.example.quickmark.ui.theme.Constants.FILE_NAME

sealed class Screen(val route: String) {
    object Home: Screen("HOME_SCREEN")
    object Setting: Screen("SETTING_SCREEN")
    object AddEditNote: Screen("ADD_EDIT_NOTE_SCREEN"){
        val full = "$route?$FILE_NAME={$FILE_NAME}"
        fun navArg(fileName: String = "") = "$route?$FILE_NAME=$fileName"
    }
}