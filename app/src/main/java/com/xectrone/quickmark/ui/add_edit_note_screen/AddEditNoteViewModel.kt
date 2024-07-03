package com.xectrone.quickmark.ui.add_edit_note_screen

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xectrone.quickmark.domain.Util
import com.xectrone.quickmark.domain.file_handling.DataStore.getSavedDirectoryUri
import com.xectrone.quickmark.domain.file_handling.SAFFileHelper
import kotlinx.coroutines.launch

class AddEditNoteViewModel(application: Application): AndroidViewModel(application) {

    private val _noteContent = mutableStateOf<String>("")
    val noteContent: State<String> = _noteContent

    private val _noteTitle = mutableStateOf<String>(Util.defaultFileName())
    val noteTitle: State<String> = _noteTitle

    private val _fileName = mutableStateOf<String>("")
    val fileName: State<String> = _fileName

    private val _fileUri = mutableStateOf<Uri?>(null)
    val fileUri: State<Uri?> = _fileUri

    private val _isNewNote = mutableStateOf<Boolean>(true)
    val isNewNote: State<Boolean> = _isNewNote

    private val _isFileModified = mutableStateOf<Boolean>(true)
    val isFileModified: State<Boolean> = _isFileModified

    private val _directoryUri = mutableStateOf<Uri?>(null)
    val directoryUri: State<Uri?> = _directoryUri

    init {
        observeDirectoryUri()
    }

    private fun observeDirectoryUri() {
        viewModelScope.launch {
            getSavedDirectoryUri(getApplication())?.let{ uri ->
                _directoryUri.value = uri
            }
        }
    }


    fun onNoteContentChange(value:String){
        _noteContent.value = value
        if (!isNewNote.value)
            _isFileModified.value = fileUri.value?.let { SAFFileHelper.isFileModified(newFileName = noteTitle.value, newFileContent = noteContent.value, fileUri = it, context = getApplication())}?:false
    }

    fun onNoteTitleChange(value:String){
        _noteTitle.value = value
        if (!isNewNote.value)
            _isFileModified.value = fileUri.value?.let { SAFFileHelper.isFileModified(newFileName = noteTitle.value, newFileContent = noteContent.value, fileUri = it, context = getApplication())}?:false
    }


    fun onCreateNote(){
        directoryUri.value?.let { SAFFileHelper.createFile(fileName = noteTitle.value, context = getApplication(), directoryUri = it, content = noteContent.value) }
    }

    fun onEditNote(){
        fileUri.value?.let { _fileUri.value = SAFFileHelper.editFile(context = getApplication(), fileUri = it, newFileName = noteTitle.value, newFileContent = noteContent.value) }
        fileUri.value?.let { _fileName.value = noteTitle.value }
        setContent()
    }

    fun setContent() {
        fileUri.value?.let {_noteContent.value = SAFFileHelper.getFileContent(fileUri = it, context = getApplication())}
        fileUri.value?.let {_noteTitle.value = SAFFileHelper.getFileName(fileUri = it,context = getApplication())}
        fileUri.value?.let {_fileName.value = SAFFileHelper.getFileName(fileUri = it,context = getApplication())}
        _isFileModified.value = false
    }

    fun isNoteExists(): Boolean{
        if (noteTitle.value == fileName.value)
            return false
        val isExists = directoryUri.value?.let { it1 -> SAFFileHelper.isFileExists(newFileName = noteTitle.value, directoryUri = it1, context = getApplication()) }
        return isExists?:true
    }

    fun toggleIsNewNote(){
        _isNewNote.value = false
        _isFileModified.value = false
    }

    fun setFileUri(fileUri: String){
        _fileUri.value = Uri.parse(fileUri)
    }
}