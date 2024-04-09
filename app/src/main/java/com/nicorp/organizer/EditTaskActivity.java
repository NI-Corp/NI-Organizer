package com.nicorp.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class EditTaskActivity extends AppCompatActivity {

    private Task task;
    private TextView titleEditText, descriptionEditText;
    private Button saveButton, backButton;

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
                titleEditText = findViewById(R.id.titleEditText);
                descriptionEditText = findViewById(R.id.descriptionEditText);
                titleEditText.setText(task.getTitle());
                descriptionEditText.setText(task.getDescription());
                backButton = findViewById(R.id.backButton);
                saveButton = findViewById(R.id.saveButton);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveChanges();
                        finish();
                    }
                });
            }
        }
    }

    private void saveChanges() {
        // Get the updated title and description from the EditText fields
        String updatedTitle = titleEditText.getText().toString().trim();
        String updatedDescription = descriptionEditText.getText().toString().trim();

        // Update the task object
        task.setTitle(updatedTitle);
        task.setDescription(updatedDescription);

        // TODO: Save the updated task to the database or SharedPreferences

    }


}