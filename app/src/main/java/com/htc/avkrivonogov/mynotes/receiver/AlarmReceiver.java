package com.htc.avkrivonogov.mynotes.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.htc.avkrivonogov.mynotes.SingleTaskActivity;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");

        Intent alarmIntent = new Intent(context, SingleTaskActivity.class);
        alarmIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
    }
}
