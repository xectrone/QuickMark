package com.example.quickmark.ui.add_note_dialog.add_note_dialog

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmark.domain.Util
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.file_handling.DataStore
import com.example.quickmark.domain.file_handling.FileHelper
import com.example.quickmark.domain.file_handling.SAFFileHelper
import kotlinx.coroutines.launch

class AddNoteViewModel(application: Application): AndroidViewModel(application) {
    private val _noteContent = mutableStateOf<String>("")
    val noteContent: State<String> = _noteContent

    private val _noteTitle = mutableStateOf<String>(Util.defaultFileName())
    val noteTitle: State<String> = _noteTitle

    private val _directoryUri = mutableStateOf<Uri?>(null)
    val directoryUri: State<Uri?> = _directoryUri


    init {
        observeDirectoryUri()
    }

    private fun observeDirectoryUri() {
        viewModelScope.launch {
            DataStore.getSavedDirectoryUri(getApplication())?.let{ uri ->
                _directoryUri.value = uri
            }
        }
    }

    fun onNoteContentChange(value:String){
        _noteContent.value = value
    }

    fun onNoteTitleChange(value:String){
        _noteTitle.value = value
    }

    fun onSaveClick(){
        directoryUri.value?.let { SAFFileHelper.createFile(fileName = noteTitle.value, content = noteContent.value, directoryUri = it, context = getApplication() ) }
    }

    fun isNoteExists(fileName: String): Boolean{
        return directoryUri.value?.let { SAFFileHelper.isFileExists(newFileName = fileName,directoryUri = it, context = getApplication()) }?:true
    }
}