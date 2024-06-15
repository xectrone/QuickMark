package com.example.quickmark.ui.home_screen

import android.net.Uri
import java.io.File

data class NoteSelectionListItem(
    val note: File,
    val fileUri: Uri,
    val isSelected: Boolean = false
)
