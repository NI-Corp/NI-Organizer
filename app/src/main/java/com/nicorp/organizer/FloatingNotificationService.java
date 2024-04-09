package com.nicorp.organizer;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class FloatingNotificationService extends Service {

    private WindowManager windowManager;
    private View floatingView;

    public FloatingNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Inflate the floating view
        floatingView = LayoutInflater.from(this).inflate(R.layout.notification_layout, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

        String task_title = intent.getStringExtra("task_title");
        String task_description = intent.getStringExtra("task_description");
        String task_start_time = intent.getStringExtra("task_start_time");
        Log.d("FloatingNotificationService", "Task completed: " + task_title + " " + task_description + " " + task_start_time);

        TextView notification_title = floatingView.findViewById(R.id.notification_title);
        TextView notification_content = floatingView.findViewById(R.id.notification_content);
        TextView startTime = floatingView.findViewById(R.id.startTime);

        notification_title.setText(task_title);
        notification_content.setText(task_description);
        startTime.setText(task_start_time);

        ImageView acceptButton = floatingView.findViewById(R.id.acceptButton);
        ImageView snoozeButton = floatingView.findViewById(R.id.snoozeButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Закрыть notification_layout и выполнить необходимые действия
                stopSelf(); // Останавливает сервис и убирает плавающее окно
            }
        });

        snoozeButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, SnoozeActivity.class);
            intent1.putExtra("task_title", task_title);
            intent1.putExtra("task_description", task_description);
            intent1.putExtra("task_start_time", task_start_time);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            stopSelf();
        });

        return START_NOT_STICKY;
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setNewAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class); // AlarmReceiver - это ваш BroadcastReceiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

            @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }
}