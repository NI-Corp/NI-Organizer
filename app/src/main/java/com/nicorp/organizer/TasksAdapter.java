package com.nicorp.organizer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private List<Task> tasks;

    public TasksAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.taskTime.setText(formatTime(task.getDateTime()));
        holder.itemColor.setBackgroundColor(task.getColor());

        holder.taskMainLayout.setOnClickListener(v -> {
            // Go to the task details screen with putExtra params
            Intent taskDetailsIntent = new Intent(v.getContext(), EditTaskActivity.class);
            taskDetailsIntent.putExtra("task", task);
            v.getContext().startActivity(taskDetailsIntent);
        });

        long currentTime = System.currentTimeMillis();
        long taskTime = task.getDateTime();
        long timeDifference = taskTime - currentTime;
        long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        holder.remainingTime.setText(minutesRemaining + " мин");
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, taskTime, remainingTime;
        ConstraintLayout taskMainLayout, itemColor;

        TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            description = itemView.findViewById(R.id.taskDescription);
            taskTime = itemView.findViewById(R.id.taskTime);
            remainingTime = itemView.findViewById(R.id.remainingTime);
            taskMainLayout = itemView.findViewById(R.id.taskMainLayout);
            itemColor = itemView.findViewById(R.id.itemColor);
            }
    }

    private String formatTime(long dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(dateTime));
    }
}
