package com.example.quickmark.domain.file_handling

import android.content.Context
import android.net.Uri
import android.util.Log

object DataStore {
    fun saveSelectedDirectoryUri(context: Context, uri: Uri) {
        Log.d("#TAG", "saveSelectedDirectoryUri: $uri")
        val prefs = context.getSharedPreferences("QuickMarkPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("selected_directory_uri", uri.toString()).apply()
    }

    fun getSavedDirectoryUri(context: Context): Uri? {
        val prefs = context.getSharedPreferences("QuickMarkPrefs", Context.MODE_PRIVATE)
        return prefs.getString("selected_directory_uri", null)?.let { Uri.parse(it) }
    }
}