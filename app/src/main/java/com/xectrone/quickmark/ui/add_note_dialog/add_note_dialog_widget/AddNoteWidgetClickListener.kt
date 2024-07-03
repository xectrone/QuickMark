package com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xectrone.quickmark.ui.add_note_dialog.AddNoteActivity

class AddNoteWidgetClickListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val newIntent = Intent(context, AddNoteActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(newIntent)
    }
}