package com.example.quickmark.ui.add_edit_note_screen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.file_handling.FileHelper
import kotlinx.coroutines.launch

class AddEditNoteViewModel(application: Application): AndroidViewModel(application) {
    private val dataStoreManager: DataStoreManager = DataStoreManager.getInstance(application)

    private val directoryPath = mutableStateOf<String>("")

    private val _noteContent = mutableStateOf<String>("")
    val noteContent: State<String> = _noteContent

    private val _noteTitle = mutableStateOf<String>("")
    val noteTitle: State<String> = _noteTitle

    private val _edit = mutableStateOf<Boolean>(true)
    val edit: State<Boolean> = _edit

    val fileHelper = FileHelper(directoryPath = directoryPath.value)

    init {
        getDirectoryPath()
    }

    private fun getDirectoryPath() {
        viewModelScope.launch {
            dataStoreManager.directoryPathFlow.collect { path ->
                if (!path.isNullOrEmpty()) {
                    directoryPath.value = path
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

    fun onSaveClick(){
        _edit.value = false
        fileHelper.saveMarkdownFile(fileName = noteTitle.value, content = noteContent.value)
        Log.d("#TAG", "onSaveClick: ${noteTitle.value}")
    }
}