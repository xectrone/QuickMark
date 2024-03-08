package com.example.quickmark

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.flashup.ui.theme.QuickMarkTheme
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.navigation.HomeScreenNavGraph
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            QuickMarkTheme {
                HomeScreenNavGraph(navController = rememberNavController())
            }
        }
    }

}





