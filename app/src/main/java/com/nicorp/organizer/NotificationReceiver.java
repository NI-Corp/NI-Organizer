package com.nicorp.organizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // Действия при получении уведомления
        Toast.makeText(context, title + ": " + message, Toast.LENGTH_SHORT).show();
    }
}
