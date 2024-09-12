package com.xectrone.quickmark.ui.home_screen

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xectrone.quickmark.domain.file_handling.DataStore.getSavedDirectoryUri
import com.xectrone.quickmark.domain.file_handling.DataStore.getSavedSort
import com.xectrone.quickmark.domain.file_handling.DataStore.saveSelectedSort
import com.xectrone.quickmark.domain.file_handling.SAFFileHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _directoryUri = mutableStateOf<Uri?>(null)
    val directoryUri: State<Uri?> = _directoryUri

    private val _sortOption = mutableStateOf<Int?>(0)
    val sortOption: State<Int?> = _sortOption

    private val _isExpanded = mutableStateOf<Boolean>(false)
    val isExpanded: State<Boolean> = _isExpanded

    private val _markdownFilesList = MutableStateFlow<List<NoteSelectionListItem>>(emptyList())
    val markdownFilesList: StateFlow<List<NoteSelectionListItem>> = _markdownFilesList

    private val _selectionMode = mutableStateOf(false)
    val selectionMode: State<Boolean> = _selectionMode

    init {
        observeDirectoryUri()
        observeSortOption()
    }

    fun observeDirectoryUri() {
        viewModelScope.launch {
            getSavedDirectoryUri(getApplication())?.let{ uri ->
                _directoryUri.value = uri
                uri?.let {
                    refreshMarkdownFiles()
                }
            }
        }
    }

    fun observeSortOption() {
        viewModelScope.launch {
            getSavedSort(getApplication())?.let{
                if(sortOption.value != it) {
                    _sortOption.value = it
                    refreshMarkdownFiles()
                }
            }
        }
    }

    suspend fun refreshMarkdownFiles() {
        directoryUri.value?.let{uri ->
            _markdownFilesList.value = when(sortOption.value){
                SortOptions.nameASC -> SAFFileHelper.getMarkdownFilesFromDirectory(directoryUri = uri, context = getApplication()).sortedBy { it.fileName }
                SortOptions.nameDESC -> SAFFileHelper.getMarkdownFilesFromDirectory(directoryUri = uri, context = getApplication()).sortedBy { it.fileName }.reversed()
                SortOptions.lastModifiedASC -> SAFFileHelper.getMarkdownFilesFromDirectory(directoryUri = uri, context = getApplication()).sortedBy { it.lastModified }
                SortOptions.lastModifiedDESC -> SAFFileHelper.getMarkdownFilesFromDirectory(directoryUri = uri, context = getApplication()).sortedBy { it.lastModified }.reversed()
                else -> SAFFileHelper.getMarkdownFilesFromDirectory(directoryUri = uri, context = getApplication()).sortedBy { it.lastModified }.reversed()
            }

        }
    }

    fun onDelete() {
        viewModelScope.launch {
            SAFFileHelper.deleteSelectedFiles(noteSelectionListItems = markdownFilesList.value, context =  getApplication())
            refreshMarkdownFiles()
        }
        _selectionMode.value = false
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

    fun onClear() {
        if (selectionMode.value) {
            _markdownFilesList.value = _markdownFilesList.value.map { it.copy(isSelected = false) }
            _selectionMode.value = false
        }
    }

    fun onSort(sortOption:Int){
        viewModelScope.launch {
            saveSelectedSort(context = getApplication(), sortOption = sortOption)
        }
        observeSortOption()
        hideMenu()
    }

    fun showMenu(){
        _isExpanded.value = true
        onClear()
    }

    fun hideMenu(){
        _isExpanded.value = false
    }
}