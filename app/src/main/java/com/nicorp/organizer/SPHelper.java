package com.nicorp.organizer;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SPHelper {
    public static void saveTask(Task SingleTask, Context context) {
        // TODO: Save the task to SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("Tasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonTasks = sharedPreferences.getString("tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> tasks;
        if (jsonTasks == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(jsonTasks, type);
        }
        tasks.add(SingleTask);
        String json = gson.toJson(tasks);
        editor.putString("tasks", json);
        editor.apply();
    }

    public static Task getTask(int taskId, Context context) {
        // TODO: Get the task from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("Tasks", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonTasks = sharedPreferences.getString("tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> tasks;
        if (jsonTasks == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(jsonTasks, type);
        }
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    public static void deleteTask(int taskId, Context context) {
        // Ask the user if they want to delete the task
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // TODO: Delete the task from SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("Tasks", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String jsonTasks = sharedPreferences.getString("tasks", null);
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            ArrayList<Task> tasks;
            if (jsonTasks == null) {
                tasks = new ArrayList<>();
            } else {
                tasks = gson.fromJson(jsonTasks, type);
            }
            for (Task task : tasks) {
                if (task.getTaskId() == taskId) {
                    tasks.remove(task);
                    break;
                }
            }
            String json = gson.toJson(tasks);
            editor.putString("tasks", json);
            editor.apply();
            // finish the activity
            ((Activity)context).finish();
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public static void changeTask(int taskId, String title, String description, long dateTime, Context context) {
        // TODO: Change the task in SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("Tasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonTasks = sharedPreferences.getString("tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> tasks;
        if (jsonTasks == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(jsonTasks, type);
        }
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                task.setTitle(title);
                task.setDescription(description);
                task.setDateTime(dateTime);
                String json = gson.toJson(tasks);
                editor.putString("tasks", json);
                editor.apply();
                break;
            }
        }
    }

    public static int getGroupCount(Context baseContext) {
        // TODO: Get the max group id
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences("Tasks", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonTasks = sharedPreferences.getString("tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> tasks;
        if (jsonTasks == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(jsonTasks, type);
        }
        int maxGroupId = 0;
        for (Task task : tasks) {
            if (task.getGroupId() > maxGroupId) {
                maxGroupId = task.getGroupId();
            }
        }
        return maxGroupId;
    }
}
