package com.example.quickmark.ui.add_note_dialog.add_note_dialog_widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.compose.ui.res.stringResource
import com.example.quickmark.R

class QuickMarkWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            val clickIntent = Intent(context, AddNoteWidgetClickListener::class.java).apply {
                action = context.resources.getString(R.string.add_note_widget_click_intent)
            }
            val pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
