package com.htc.avkrivonogov.mynotes.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    String INTENT_NOTE = "note";

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    AlarmManager alarmManager = (AlarmManager) context
}
