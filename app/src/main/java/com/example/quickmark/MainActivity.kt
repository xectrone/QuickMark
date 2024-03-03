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
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.flashup.ui.theme.QuickMarkTheme
import com.example.quickmark.domain.navigation.HomeScreenNavGraph
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            QuickMarkTheme {
                HomeScreenNavGraph(navController = rememberNavController())
            }
        }
        addHomeScreenShortcut()
    }
    private fun addHomeScreenShortcut() {
        val shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager

        // Create an intent to launch the dialog activity
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.action = Intent.ACTION_CREATE_SHORTCUT

        // Create a shortcut info
        val shortcutInfo = ShortcutInfo.Builder(this, "new_note_shortcut")
            .setShortLabel("New Note")
            .setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground))
            .setIntent(intent)
            .build()

        // Check if shortcut already exists
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!shortcutManager.isRequestPinShortcutSupported) {
                Toast.makeText(this, "Pin shortcut is not supported", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Request to pin the shortcut
        shortcutManager.requestPinShortcut(shortcutInfo, null)
    }
}



