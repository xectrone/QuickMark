package com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_shortcut

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.Toast
import com.xectrone.quickmark.R
import com.xectrone.quickmark.ui.add_note_dialog.AddNoteActivity

fun addHomeScreenShortcut(context: Context) {
    val shortcutManager = context.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager

    // Create an intent to launch the dialog activity
    val intent = Intent(context, AddNoteActivity::class.java)
    intent.action = Intent.ACTION_CREATE_SHORTCUT

    // Create a shortcut info
    val shortcutInfo = ShortcutInfo.Builder(context, "new_note_shortcut")
        .setShortLabel("Add Note")
        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
        .setIntent(intent)
        .build()

    // Check if shortcut already exists
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (!shortcutManager.isRequestPinShortcutSupported) {
            Toast.makeText(context, "Pin shortcut is not supported", Toast.LENGTH_SHORT).show()
            return
        }
        else
            Toast.makeText(context, "Shortcut is Created !", Toast.LENGTH_SHORT).show()
    }
    shortcutManager.requestPinShortcut(shortcutInfo, null)
}