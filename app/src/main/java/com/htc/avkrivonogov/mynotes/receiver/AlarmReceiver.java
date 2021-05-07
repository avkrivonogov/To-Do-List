package com.htc.avkrivonogov.mynotes.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        DatabaseHelper  dbh = new DatabaseHelper(context);
    }
}
