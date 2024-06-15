package com.example.quickmark

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.flashup.ui.theme.QuickMarkTheme
import com.example.quickmark.data.datastore.DataStoreManager
import com.example.quickmark.domain.navigation.HomeScreenNavGraph
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity
import com.example.quickmark.ui.theme.Constants
import com.example.quickmark.ui.utility.MessageScreen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_QuickMark)
        setContent {
            QuickMarkTheme {
                HomeScreenNavGraph(navController = rememberNavController())
            }

        }
    }

}





