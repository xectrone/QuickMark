package com.example.quickmark.ui.add_note_dialog

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.quickmark.R


class WidgetClickListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val newIntent = Intent(context, AddNoteActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(newIntent)
    }
}

class MyAppWidgetProvider : AppWidgetProvider() {
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

            val clickIntent = Intent(context, WidgetClickListener::class.java).apply {
                action = "com.example.myapplication.WIDGET_ACTION_CLICK"
            }
            val pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0)
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
