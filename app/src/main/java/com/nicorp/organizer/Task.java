package com.nicorp.organizer;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class Task implements Serializable {
    private int taskId;
    private String title;
    private String description;
    private long dateTime;
    private int color;
;   private int groupId = 0;
    private boolean isRecurring;

    // Constructor for non-recurring task
    public Task(String title, String description, int color, long dateTime) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.color = color;
        this.isRecurring = false;
        this.taskId = generateUniqueId(); // Здесь generateUniqueId() - ваш метод для генерации уникального идентификатора

        Log.d("TaskId", String.valueOf(taskId));
    }

    private int generateUniqueId() {
        // Генерация уникального идентификатора с помощью UUID
        UUID uuid = UUID.randomUUID();
        return uuid.hashCode(); // Вернуть хэш-код UUID в виде уникального идентификатора
    }

    // Constructor for recurring task
    public Task(String title, String description, int color, long dateTime, boolean isRecurring, int groupId) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.color = color;
        this.isRecurring = isRecurring;
        this.groupId = groupId;
        this.taskId = generateUniqueId(); // Здесь generateUniqueId() - ваш метод для генерации уникального идентификатора
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", isRecurring=" + isRecurring +
                '}';
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
