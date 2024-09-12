package com.xectrone.quickmark.data

import android.content.Context
import android.net.Uri

object DataStore {
    fun saveSelectedDirectoryUri(context: Context, uri: Uri) {
        val prefs = context.getSharedPreferences("QuickMarkPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("selected_directory_uri", uri.toString()).apply()
    }

    fun getSavedDirectoryUri(context: Context): Uri? {
        val prefs = context.getSharedPreferences("QuickMarkPrefs", Context.MODE_PRIVATE)
        return prefs.getString("selected_directory_uri", null)?.let { Uri.parse(it) }
    }

    fun saveSelectedSort(context: Context, sortOption: Int) {
        val prefs = context.getSharedPreferences("QuickMarkPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("selected_sort_option", sortOption).apply()
    }

    fun getSavedSort(context: Context): Int? {
        val prefs = context.getSharedPreferences("QuickMarkPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("selected_sort_option", 0)
    }
}