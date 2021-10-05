package com.example.pozivnik;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class widgetService extends Service {
    public widgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        String state = preferences.getString("state","Aktivno");
        if(state.equals("Aktivno")){
            editor.putString("state","Tiho");
            editor.apply();
        }else{
            editor.putString("state","Aktivno");
            editor.apply();
        }
        Intent intent = new Intent(this, appWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), appWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
        this.stopSelf();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
