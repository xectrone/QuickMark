package com.example.quickmark.ui.home_screen

import android.app.Application
import android.os.FileObserver
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.file_handling.FileHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager: DataStoreManager = DataStoreManager.getInstance(application)

    private val _directoryPath = MutableStateFlow<String>("")
    val directoryPath: StateFlow<String> = _directoryPath

    private val _markdownFilesList = MutableStateFlow<List<NoteSelectionListItem>>(emptyList())
    val markdownFilesList: StateFlow<List<NoteSelectionListItem>> = _markdownFilesList

    private val _selectionMode = mutableStateOf(false)
    val selectionMode: State<Boolean> = _selectionMode

    private var fileObserver: FileObserver? = null
    private lateinit var fileHelper: FileHelper


    init {
        observeDirectoryPath()
    }

    private fun observeDirectoryPath() {
        viewModelScope.launch {
            dataStoreManager.directoryPathFlow.collect { path ->
                if (!path.isNullOrEmpty()) {
                    _directoryPath.value = path
                    fileHelper = FileHelper(directoryPath = _directoryPath.value)
                    startFileObserver()
                    refreshMarkdownFiles()
                }
            }
        }
    }

    private fun startFileObserver() {
        fileObserver?.stopWatching()
        fileObserver = object : FileObserver(File(_directoryPath.value), CREATE or DELETE or MODIFY) {
            override fun onEvent(event: Int, path: String?) {
                viewModelScope.launch {
                    refreshMarkdownFiles()
                }
            }
        }
        fileObserver?.startWatching()
    }

    private suspend fun refreshMarkdownFiles() {
        val directory = File(_directoryPath.value)
        val markdownFilesList = mutableListOf<File>()
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (file.isFile && file.extension.equals("md", ignoreCase = true)) {
                    markdownFilesList.add(file)
                }
            }
        }
        _markdownFilesList.value = markdownFilesList.map { NoteSelectionListItem(it) }
    }

    override fun onCleared() {
        super.onCleared()
        fileObserver?.stopWatching()
    }

    fun onDelete() {
        viewModelScope.launch {
            markdownFilesList.value.filter { it.isSelected }
                .forEach { item -> fileHelper.deleteMarkdownFile(item.note.name, getApplication()) }
            _selectionMode.value = false
        }
    }

    fun onItemClick(item: NoteSelectionListItem) {
        _markdownFilesList.value = markdownFilesList.value.map { if (it == item) it.copy(isSelected = !it.isSelected) else it }
        if (_markdownFilesList.value.none { it.isSelected })
            _selectionMode.value = false
    }

    fun onItemLongClick(item: NoteSelectionListItem) {
        if (!selectionMode.value) {
            _selectionMode.value = true
            _markdownFilesList.value = markdownFilesList.value.map { if (it == item) it.copy(isSelected = !it.isSelected) else it }
        }
    }

    fun onClear(){
        if(selectionMode.value){
            _markdownFilesList.value = _markdownFilesList.value.map { NoteSelectionListItem(it.note) }
            _selectionMode.value = false
        }
    }
}
