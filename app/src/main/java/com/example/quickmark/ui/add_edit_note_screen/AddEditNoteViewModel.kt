package com.example.quickmark.ui.add_edit_note_screen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmark.data.Util
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.file_handling.FileHelper
import kotlinx.coroutines.launch

class AddEditNoteViewModel(application: Application): AndroidViewModel(application) {
    private val dataStoreManager: DataStoreManager = DataStoreManager.getInstance(application)

    private val directoryPath = mutableStateOf<String>("")

    private val _noteContent = mutableStateOf<String>("")
    val noteContent: State<String> = _noteContent

    private val _noteTitle = mutableStateOf<String>(Util.defaultFileName())
    val noteTitle: State<String> = _noteTitle

    private val _fileName = mutableStateOf<String>("")
    val fileName: State<String> = _fileName

    private val _isNewNote = mutableStateOf<Boolean>(true)
    val isNewNote: State<Boolean> = _isNewNote

    private val _isFileModified = mutableStateOf<Boolean>(true)
    val isFileModified: State<Boolean> = _isFileModified

    private lateinit var fileHelper: FileHelper

    init {
        getDirectoryPath()
    }

    private fun getDirectoryPath() {
        viewModelScope.launch {
            dataStoreManager.directoryPathFlow.collect { path ->
                if (!path.isNullOrEmpty()) {
                    directoryPath.value = path
                    fileHelper = FileHelper(directoryPath = directoryPath.value)
                }
            }
        }
    }

    fun onNoteContentChange(value:String){
        _noteContent.value = value
        if (!isNewNote.value)
            _isFileModified.value = fileHelper.isFileModified(fileName.value, noteTitle.value, noteContent.value)
    }

    fun onNoteTitleChange(value:String){
        _noteTitle.value = value
        if (!isNewNote.value)
            _isFileModified.value = fileHelper.isFileModified(fileName.value, noteTitle.value, noteContent.value)
    }


    fun onCreateNote(){
        fileHelper.createMarkdownFile(noteTitle.value, noteContent.value, getApplication())
        toggleIsNewNote()

    }

    fun onEditNote(){
        fileHelper.editMarkdownFile(fileName.value,noteTitle.value, noteContent.value, getApplication())?.let { _fileName.value = it }
        setContent()
    }

    fun setContent() {
        _noteContent.value =  fileHelper.readMarkdownFile(fileName.value, getApplication())
        _noteTitle.value = fileName.value
        _isFileModified.value = false
    }

    fun isNoteExists(value: String): Boolean{
        return fileHelper.isFileExists(value)
    }

    fun toggleIsNewNote(){
        _isNewNote.value = false
    }

    fun setFileName(value: String){
        _fileName.value = value
    }
}