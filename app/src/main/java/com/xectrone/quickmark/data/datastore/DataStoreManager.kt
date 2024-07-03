package com.xectrone.quickmark.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("datastore")
        val DIRECTORY_PATH_KEY = stringPreferencesKey("directory_path")

        @Volatile
        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(this) {
                instance ?: DataStoreManager(context.applicationContext).also { instance = it }
            }
        }
    }

    suspend fun saveDirectoryPath(path: String) {
        context.dataStore.edit { it[DIRECTORY_PATH_KEY] = path }
    }

    val directoryPathFlow: Flow<String?> = context.dataStore.data.map { it[DIRECTORY_PATH_KEY] }
}

