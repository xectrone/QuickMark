package com.example.quickmark.ui.home_screen

import android.app.Application
import android.os.FileObserver
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val directoryPath = "/storage/emulated/0/0.MEDIA/qn"
    private val _markdownFiles = MutableStateFlow<List<File>>(emptyList())
    val markdownFiles: Flow<List<File>> = _markdownFiles.asStateFlow()

    private val fileObserver = object : FileObserver(File(directoryPath), FileObserver.CREATE or FileObserver.DELETE or FileObserver.MODIFY) {
        override fun onEvent(event: Int, path: String?) {
            viewModelScope.launch {
                refreshMarkdownFiles()
            }
        }
    }

    init {
        fileObserver.startWatching()
        viewModelScope.launch {
            refreshMarkdownFiles()
        }
    }

    private suspend fun refreshMarkdownFiles() {
        val directory = File(directoryPath)
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
}