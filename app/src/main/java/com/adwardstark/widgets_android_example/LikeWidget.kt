package com.adwardstark.widgets_android_example

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.adwardstark.widgets_android_example.LikeWidget.Companion.UPDATE_ACTION

/**
 * Implementation of App Widget functionality.
 */
class LikeWidget : AppWidgetProvider() {

    companion object {
        const val UPDATE_ACTION = "WIDGET_UPDATE_LIKE_UNLIKE"
        private var likeCounter: Int = 0
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let {
            if(it.action == UPDATE_ACTION) {
                context?.updateLike()
            }
        }
    }

    private fun Context.updateLike() {
        val widgetViews = RemoteViews(this.packageName, R.layout.like_widget)
        if(likeCounter == 0) {
            likeCounter++
            widgetViews.setImageViewResource(R.id.appwidget_like, R.drawable.ic_like_unfilled)
            Toast.makeText(this, "You don't like me :(", Toast.LENGTH_SHORT).show()
        } else {
            likeCounter--
            widgetViews.setImageViewResource(R.id.appwidget_like, R.drawable.ic_like_filled)
            Toast.makeText(this, "You like me :)", Toast.LENGTH_SHORT).show()
        }
        val widgetComponent = ComponentName(this, LikeWidget::class.java)
        val widgetManager = AppWidgetManager.getInstance(this)
        widgetManager.updateAppWidget(widgetComponent, widgetViews)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.like_widget)

    val intent = Intent(context, LikeWidget::class.java)
    intent.action = UPDATE_ACTION

    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.appwidget_like, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}