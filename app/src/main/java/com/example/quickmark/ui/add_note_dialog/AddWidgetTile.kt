package com.example.quickmark.ui.add_note_dialog

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.quickmark.R

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class NewMarkdownFileDialogTileService : TileService() {
    override fun onClick() {
        super.onClick()
        // Set up click action
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(intent)
    }

    override fun onStartListening() {
        super.onStartListening()
        // Set up tile appearance
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        // Clean up if needed
    }
}
