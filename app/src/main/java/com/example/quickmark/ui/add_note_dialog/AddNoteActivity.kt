package com.example.quickmark.ui.add_note_dialog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.flashup.ui.theme.QuickMarkTheme

class AddNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickMarkTheme {
                AddNoteDialog { finish() }
//                finish()
            }
        }
    }
}