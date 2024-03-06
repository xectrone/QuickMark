package com.example.quickmark.ui.home_screen

import android.app.Application
import android.os.FileObserver
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmark.data.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val dataStoreManager: DataStoreManager = DataStoreManager.getInstance(application)

    private val _directoryPath = mutableStateOf<String>("")
    val directoryPath: State<String> = _directoryPath

    private val _markdownFiles = MutableStateFlow<List<File>>(emptyList())
    val markdownFiles: Flow<List<File>> = _markdownFiles.asStateFlow()


    private val fileObserver = object : FileObserver(File(directoryPath.value), CREATE or DELETE or MODIFY) {
        override fun onEvent(event: Int, path: String?) {
            viewModelScope.launch {
                refreshMarkdownFiles()
            }
        }
    }

    init {
        getDirectoryPath()
        fileObserver.startWatching()
    }

    private suspend fun refreshMarkdownFiles() {
        val directory = File(directoryPath.value)
        val markdownFilesList = mutableListOf<File>()
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (file.isFile && file.extension.equals("md", ignoreCase = true)) {
                    markdownFilesList.add(file)
                }
            }
        }
        _markdownFiles.emit(markdownFilesList)
    }

    override fun onCleared() {
        super.onCleared()
        fileObserver.stopWatching()
    }

    private fun getDirectoryPath() {
        viewModelScope.launch {
            dataStoreManager.directoryPathFlow.collect { path ->
                if (!path.isNullOrEmpty()) {
                    _directoryPath.value = path
                    refreshMarkdownFiles() // Refresh markdown files when directory path changes
                }
            }
        }
    }
}