package com.htc.avkrivonogov.mynotes.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.htc.avkrivonogov.mynotes.R;
import com.htc.avkrivonogov.mynotes.SingleTaskActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Класс для оповещения.
 */
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
    String idChannel = "Reminder_channel_id";
    CharSequence name = "Reminder_channel";
    String description = "Channel for task reminder";
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    NotificationChannel channel = new NotificationChannel(idChannel, name, importance);
    channel.setDescription(description);
    channel.setShowBadge(false);
    manager.createNotificationChannel(channel);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_alarm_date)
            .setContentTitle(bundle.getString("title"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

    if (bundle.getString("description") != null) {
      builder.setContentText(bundle.getString("description"));
    }
    if (bundle.getString("image") != null) {
      Uri uri = Uri.parse(bundle.getString("image"));
      try {
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        final Bitmap selectedImages = BitmapFactory.decodeStream(imageStream);
        builder.setLargeIcon(selectedImages);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    Notification notification = builder.build();
    manager.notify(id, notification);
  }
}
