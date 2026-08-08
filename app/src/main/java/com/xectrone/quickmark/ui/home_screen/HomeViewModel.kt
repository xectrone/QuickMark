package com.xectrone.quickmark.ui.home_screen

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xectrone.quickmark.data.DataStore.getSavedSort
import com.xectrone.quickmark.data.DataStore.saveSelectedSort
import com.xectrone.quickmark.domain.file_handling.SAFFileHelper
import com.xectrone.quickmark.domain.file_handling.SAFStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        observeSortOption()
        observeDirectoryUri()
    }

    fun observeDirectoryUri() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            val uri = SAFStorageManager.getDirectoryUri(context)

            if (uri == null) {
                // No directory has been chosen yet
                _directoryUri.value = null
                return@launch
            }

            // Validate on a background thread (SAF query can be slow)
            val isValid = withContext(Dispatchers.IO) {
                SAFStorageManager.isDirectoryValid(context, uri)
            }

            if (isValid) {
                _directoryUri.value = uri
                refreshMarkdownFiles()
            } else {
                Log.w("HomeViewModel", "Stored URI is stale — clearing and redirecting to setup")
                // Release permission and clear the stale URI
                withContext(Dispatchers.IO) {
                    SAFStorageManager.releasePermission(context, uri)
                }
                SAFStorageManager.clearDirectoryUri(context)
                // Reset UI state — HomeScreen shows "Select Directory" automatically
                _directoryUri.value = null
                _markdownFilesList.value = emptyList()
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
            val files = SAFFileHelper.getMarkdownFilesFromDirectory(directoryUri = uri, context = getApplication())
            
            // Separate pinned and unpinned notes
            val pinnedNotes = files.filter { it.isPinned }
            val unpinnedNotes = files.filter { !it.isPinned }
            
            // Sort pinned and unpinned notes separately
            val sortedPinnedNotes = when(sortOption.value){
                SortOptions.nameASC -> pinnedNotes.sortedBy { it.fileName.lowercase() }
                SortOptions.nameDESC -> pinnedNotes.sortedByDescending { it.fileName.lowercase() }
                SortOptions.lastModifiedASC -> pinnedNotes.sortedBy { it.lastModified }
                SortOptions.lastModifiedDESC -> pinnedNotes.sortedByDescending { it.lastModified }
                else -> pinnedNotes.sortedByDescending { it.lastModified }
            }
            
            val sortedUnpinnedNotes = when(sortOption.value){
                SortOptions.nameASC -> unpinnedNotes.sortedBy { it.fileName.lowercase() }
                SortOptions.nameDESC -> unpinnedNotes.sortedByDescending { it.fileName.lowercase() }
                SortOptions.lastModifiedASC -> unpinnedNotes.sortedBy { it.lastModified }
                SortOptions.lastModifiedDESC -> unpinnedNotes.sortedByDescending { it.lastModified }
                else -> unpinnedNotes.sortedByDescending { it.lastModified }
            }
            
            // Combine pinned notes first, then unpinned notes
            _markdownFilesList.value = sortedPinnedNotes + sortedUnpinnedNotes
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            SAFFileHelper.deleteSelectedFiles(noteSelectionListItems = markdownFilesList.value, context =  getApplication())
            refreshMarkdownFiles()
        }
        _selectionMode.value = false
    }

    fun onSelectAll() {
        _markdownFilesList.value = markdownFilesList.value.map { it.copy(isSelected = true) }
        _selectionMode.value = true
    }

    fun onDeselectAll() {
        _markdownFilesList.value = markdownFilesList.value.map { it.copy(isSelected = false) }
        _selectionMode.value = false
    }

    fun onPinSelected() {
        viewModelScope.launch {
            val selectedItems = markdownFilesList.value.filter { it.isSelected }
            var success = true
            for (item in selectedItems) {
                val itemSuccess = SAFFileHelper.togglePinnedStatus(item.fileUri, getApplication())
                if (!itemSuccess) {
                    success = false
                }
            }
            if (success) {
                refreshMarkdownFiles()
                // Clear selection after pinning
                _selectionMode.value = false
            } else {
                Toast.makeText(getApplication(), "Failed to update some pinned statuses", Toast.LENGTH_SHORT).show()
            }
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

    // Toggle pinned status for a note
    fun togglePinnedStatus(item: NoteSelectionListItem) {
        viewModelScope.launch {
            val success = SAFFileHelper.togglePinnedStatus(item.fileUri, getApplication())
            if (success) {
                refreshMarkdownFiles()
            } else {
                Toast.makeText(getApplication(), "Failed to update pinned status", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun is_path_set() = SAFFileHelper.is_path_set(directoryUri.value, getApplication())

    fun hasFileAccessPermission(): Boolean {
        val uri = directoryUri.value ?: return false
        return SAFStorageManager.isPermissionPersisted(getApplication(), uri)
    }

}