package com.example.pozivnik;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class appWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){

            SharedPreferences preferences = context.getSharedPreferences("MyPrefs",0);
            SharedPreferences.Editor editor = preferences.edit();
            String state = preferences.getString("state","Aktivno");

            Intent intent = new Intent(context,widgetService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setTextViewText(R.id.state, state);
            views.setOnClickPendingIntent(R.id.btnWidget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }
}

