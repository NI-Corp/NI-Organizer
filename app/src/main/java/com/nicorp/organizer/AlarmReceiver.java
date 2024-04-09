package com.nicorp.organizer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.NotificationChannel;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//public class AlarmReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // Получите идентификатор задачи из интента (если используется)
//        int taskId = intent.getIntExtra("taskId", -1);
//
////        // Получите информацию о задачах из SharedPreferences
////        SharedPreferences sharedPreferences = context.getSharedPreferences("Tasks", Context.MODE_PRIVATE);
////        Gson gson = new Gson();
////        String jsonTasks = sharedPreferences.getString("tasks", null);
////        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
////        ArrayList<Task> tasks;
////        if (jsonTasks == null) {
////            tasks = new ArrayList<>();
////        } else {
////            tasks = gson.fromJson(jsonTasks, type);
////        }
////
////        // Удалите одноразовую задачу из списка
////        List<Task> updatedTasks = new ArrayList<>();
////        for (Task task : tasks) {
////            if (task.getTaskId() == taskId) {
////                // Если задача одноразовая, удалите ее
////                if (!task.isRecurring()) {
////                    Log.d("AlarmReceiver", "Deleting one-time task: " + task.getTitle());
////                    continue; // Пропустить эту задачу при добавлении в обновленный список
////                }
////            }
////            updatedTasks.add(task);
////        }
////
////        // Сохраните обновленный список задач в SharedPreferences
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        String json = gson.toJson(updatedTasks);
////        editor.putString("tasks", json);
////        editor.apply();
////
////        // Здесь также может быть код для отмены уведомления
//
//        // Выполните любые другие действия, необходимые для задачи при срабатывании уведомления
//        Log.d("AlarmReceiver", "Task completed: " + taskId);
//    }
//}

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String task_title = intent.getStringExtra("task_title");
        String task_description = intent.getStringExtra("task_description");
        String task_start_time = intent.getStringExtra("task_start_time");
        Intent serviceIntent = new Intent(context, FloatingNotificationService.class);

        Log.d("AlarmReceiver", "Task completed: " + task_title + " " + task_description + " " + task_start_time);

        serviceIntent.putExtra("task_title",task_title);
        serviceIntent.putExtra("task_description", task_description);
        serviceIntent.putExtra("task_start_time", task_start_time);
        context.startService(serviceIntent);
    }
}