package com.example.quickmark.ui.add_note_dialog.add_note_dialog_shortcut

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.example.quickmark.R
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity

fun createAppShortcut(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        val shortcut = ShortcutInfo.Builder(context, "new_markdown_file")
            .setShortLabel("New Markdown File")
            .setLongLabel("Create a new Markdown file")
            .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
            .setIntent(Intent(context, AddNoteActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            })
            .build()

        shortcutManager!!.dynamicShortcuts = listOf(shortcut)
    }
}
