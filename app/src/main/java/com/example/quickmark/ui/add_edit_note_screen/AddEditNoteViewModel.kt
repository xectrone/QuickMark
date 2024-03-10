package com.example.quickmark.ui.add_edit_note_screen

import android.app.Application
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

    private val _edit = mutableStateOf<Boolean>(true)
    val edit: State<Boolean> = _edit

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
    }

    fun onNoteTitleChange(value:String){
        _noteTitle.value = value
    }

    fun onEditClick(){
        _edit.value = true
    }


    fun onCreateNote(){
        fileHelper.createMarkdownFile(noteTitle.value, noteContent.value, getApplication())
        _edit.value = false

    }

    fun onEditNote(oldFileName:String){
        fileHelper.editMarkdownFile(oldFileName,noteTitle.value, noteContent.value, getApplication())
        _edit.value = false
    }

    fun setContent(fileName:String) {
        _noteContent.value =  fileHelper.readMarkdownFile(fileName, getApplication())
        _noteTitle.value = fileName
    }
}