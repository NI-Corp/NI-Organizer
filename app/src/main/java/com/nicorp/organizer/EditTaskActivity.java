package com.nicorp.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditTaskActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Retrieve the task object from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task")) {
            task = (Task) intent.getSerializableExtra("task");
            if (task != null) {
                // Use the task object to populate your UI or perform other actions
                // For example, you can set the task title and description in EditText fields
                EditText titleEditText = findViewById(R.id.titleEditText);
                EditText descriptionEditText = findViewById(R.id.descriptionEditText);
                titleEditText.setText(task.getTitle());
                descriptionEditText.setText(task.getDescription());
            }
        }
    }
}