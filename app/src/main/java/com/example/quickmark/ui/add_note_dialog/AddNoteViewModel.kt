package com.example.quickmark.ui.add_note_dialog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
class AddNoteViewModel: ViewModel()
{


    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    fun showAddNoteDialog() {
        _showDialog.value = true
    }

    fun dismissAddNoteDialog() {
        _showDialog.value = false
    }

}