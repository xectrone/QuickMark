package com.example.quickmark.ui.add_note_dialog.add_note_dialog_tile

import android.content.Intent
import android.graphics.drawable.Icon

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.res.painterResource
import com.example.quickmark.R
import com.example.quickmark.ui.add_note_dialog.AddNoteActivity

class AddNoteDialogService : TileService() {
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
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_foreground_white)
        qsTile.label = "Add Note"
        qsTile.subtitle = "QuickMark"
        qsTile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        // Clean up if needed
    }
}
