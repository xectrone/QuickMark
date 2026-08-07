package com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_widget

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.xectrone.quickmark.ui.add_note_dialog.AddNoteActivity

class AddNoteWidgetClickListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val newIntent = Intent(context, AddNoteActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 14+ safe background launch configuration
            val options = ActivityOptions.makeBasic().apply {
                setPendingIntentBackgroundActivityStartMode(
                    ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                )
            }
            context.startActivity(newIntent, options.toBundle())
        } else {
            // Older versions
            context.startActivity(newIntent)
        }
    }
}
