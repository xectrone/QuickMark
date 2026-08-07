package com.xectrone.quickmark.ui.add_note_dialog.add_note_dialog_tile

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.xectrone.quickmark.R
import com.xectrone.quickmark.ui.add_note_dialog.AddNoteActivity

class AddNoteDialogService : TileService() {
    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()

        val intent = Intent(this, AddNoteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                startActivityAndCollapse(pendingIntent) //
            } else {
                @Suppress("DEPRECATION")
                startActivityAndCollapse(intent) //
            }
        } catch (exception: UnsupportedOperationException) {
            try {
                startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(this, "Unable to open QuickMark from Quick Settings", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
