package com.example.quickmark.ui.settings_screen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmark.data.datastore.DataStoreManager
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {
    private val dataStoreManager: DataStoreManager = DataStoreManager.getInstance(application)

    private val _directoryPath = mutableStateOf<String>("")
    val directoryPath: State<String> = _directoryPath

    private val _editDirectoryPath = mutableStateOf<Boolean>(false)
    val editDirectoryPath: State<Boolean> = _editDirectoryPath

    init {
        getDirectoryPath()
    }

    fun getDirectoryPath() {
        viewModelScope.launch {
            dataStoreManager.directoryPathFlow.collect { path ->
                if (!path.isNullOrEmpty()) {
                    _directoryPath.value = path
                }
            }
        }
    }

    fun setDirectoryPath(value: String){
        _directoryPath.value = value
        viewModelScope.launch {
            dataStoreManager.saveDirectoryPath(value)
        }
    }

    fun onEditDirectoryPathClick(){
        _editDirectoryPath.value = true
    }
    fun onSaveDirectoryPathClick(){
        _editDirectoryPath.value = false
    }

}