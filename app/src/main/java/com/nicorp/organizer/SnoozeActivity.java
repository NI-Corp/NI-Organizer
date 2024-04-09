package com.nicorp.organizer;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SnoozeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Не устанавливаем setContentView, так как Activity используется только для показа диалога

        // Показываем DatePickerDialog сразу после запуска Activity
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> showTimePickerDialog(year, month, dayOfMonth),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnCancelListener(dialog -> finish()); // Закрыть Activity при отмене
        datePickerDialog.show();
    }

    private void showTimePickerDialog(int year, int month, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        @SuppressLint("ScheduleExactAlarm") TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    // Логика для обработки выбранного времени
                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(Calendar.YEAR, year);
                    selectedTime.set(Calendar.MONTH, month);
                    selectedTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minute);
                    long selectedTimeInMillis = selectedTime.getTimeInMillis();

                    // Установка AlarmManager для показа уведомления в заданное время
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(this, AlarmReceiver.class); // AlarmReceiver - это ваш BroadcastReceiver
                    // Передайте дополнительные данные в Intent, если необходимо
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedTimeInMillis, pendingIntent);

                    finish(); // Закрыть Activity после выбора времени
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        timePickerDialog.setOnCancelListener(dialog -> finish()); // Закрыть Activity при отмене
        timePickerDialog.show();
    }
}