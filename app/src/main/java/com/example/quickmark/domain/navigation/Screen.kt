package com.example.quickmark.domain.navigation

import android.net.Uri
import com.example.quickmark.domain.Util
import com.example.quickmark.ui.theme.Constants.FILE_URI

sealed class Screen(val route: String) {
    object Home: Screen("HOME_SCREEN")
    object Setting: Screen("SETTING_SCREEN")
    object AddEditNote: Screen("ADD_EDIT_NOTE_SCREEN"){
        val full = "$route?$FILE_URI={$FILE_URI}"
        fun navArg(fileUri: Uri? = null) = "$route?$FILE_URI=${Util.encodeUri(fileUri)}"
    }
}