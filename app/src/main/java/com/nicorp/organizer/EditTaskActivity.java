package com.nicorp.organizer;

import static com.nicorp.organizer.SPHelper.changeTask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    private Task task;
    private TextView titleEditText, descriptionEditText, startTimeTextView, startDateTextView;
    private ImageView saveButton, cancelButton, deleteButton;
    private CheckBox checkboxSunday, checkboxMonday, checkboxTuesday, checkboxWednesday, checkboxThursday, checkboxFriday, checkboxSaturday;
    private RadioGroup radioGroup;
    private ScrollView recurringOptionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Retrieve the task object from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task")) {
            task = (Task) intent.getSerializableExtra("task");
            if (task != null) {
                // Initialize UI elements
                titleEditText = findViewById(R.id.titleEditText);
                descriptionEditText = findViewById(R.id.descriptionEditText);
                startTimeTextView = findViewById(R.id.startTimeTextView);
                cancelButton = findViewById(R.id.cancelButton);
                saveButton = findViewById(R.id.saveButton);
                startDateTextView = findViewById(R.id.startDateTextView);
                recurringOptionsLayout = findViewById(R.id.recurringOptionsLayout);
                deleteButton = findViewById(R.id.deleteButton);

                deleteButton.setOnClickListener(v -> {
                    deleteTask();
                });

                // Initialize checkboxes
                checkboxSunday = findViewById(R.id.checkboxSunday);
                checkboxMonday = findViewById(R.id.checkboxMonday);
                checkboxTuesday = findViewById(R.id.checkboxTuesday);
                checkboxWednesday = findViewById(R.id.checkboxWednesday);
                checkboxThursday = findViewById(R.id.checkboxThursday);
                checkboxFriday = findViewById(R.id.checkboxFriday);
                checkboxSaturday = findViewById(R.id.checkboxSaturday);

                // Set data to text
                titleEditText.setText(task.getTitle());
                descriptionEditText.setText(task.getDescription());

                // get Time from task.getDateTime() (long)
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String time = sdf.format(task.getDateTime());
                // set time to textView
                startTimeTextView.setText(time);

                if (!task.isRecurring()) {
                    setRecurringOptionsVisibility(false);

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(task.getDateTime());

                    // get date from task.getDateTime() (long)
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime());
                    startDateTextView.setText(date);
                } else {
                    // find all tasks with same group id by SPHelper
                    ArrayList<Task> tasksWithSameGroupId = SPHelper.getTasksWithSameGroupId(task.getGroupId(), this);

                    // Log names of all tasks with same group id
                    for (Task taskWithSameGroupId : tasksWithSameGroupId) {
                        Log.d("taskWithSameGroupId", String.valueOf(taskWithSameGroupId.getDateTime()));
                    }

                    // set visibility
                    setRecurringOptionsVisibility(true);

                    // set checkboxes for recurring tasks
                    for (Task taskWithSameGroupId : tasksWithSameGroupId) {
                        if (taskWithSameGroupId.getTaskId() == task.getTaskId()) {
                            continue;
                        } else {
                            if (taskWithSameGroupId.isRecurring()) {
                                // if the day of the week is the same, set the checkbox to checked
                                // convert taskWithSameGroupId.getDateTime() (long) to Calendar
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(taskWithSameGroupId.getDateTime());
                                // get the day of the week
                                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                                Log.d("dayOfWeek", String.valueOf(dayOfWeek));
                                // do the same for task.getDateTime() (long)
                                cal.setTimeInMillis(task.getDateTime());
                                int dayOfWeek2 = cal.get(Calendar.DAY_OF_WEEK);
                                Log.d("dayOfWeek2", String.valueOf(dayOfWeek2));
                                // check in checkboxes if the day of the week is the same for both tasks
                                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek2 == Calendar.SUNDAY) {
                                    checkboxSunday.setChecked(true);
                                }  if (dayOfWeek == Calendar.MONDAY || dayOfWeek2 == Calendar.MONDAY) {
                                    checkboxMonday.setChecked(true);
                                }  if (dayOfWeek == Calendar.TUESDAY || dayOfWeek2 == Calendar.TUESDAY) {
                                    checkboxTuesday.setChecked(true);
                                }  if (dayOfWeek == Calendar.WEDNESDAY || dayOfWeek2 == Calendar.WEDNESDAY) {
                                    checkboxWednesday.setChecked(true);
                                }  if (dayOfWeek == Calendar.THURSDAY || dayOfWeek2 == Calendar.THURSDAY) {
                                    checkboxThursday.setChecked(true);
                                }  if (dayOfWeek == Calendar.FRIDAY || dayOfWeek2 == Calendar.FRIDAY) {
                                    checkboxFriday.setChecked(true);
                                }  if (dayOfWeek == Calendar.SATURDAY || dayOfWeek2 == Calendar.SATURDAY) {
                                    checkboxSaturday.setChecked(true);
                                }
                            }
                        }
                    }
                }

                cancelButton.setOnClickListener(new View.OnClickListener() {
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

    private void setRecurringOptionsVisibility(boolean visible) {
        int visibility = visible ? android.view.View.VISIBLE : android.view.View.GONE;
        recurringOptionsLayout.setVisibility(visibility);
        int divisibility = visible ?  android.view.View.GONE :android.view.View.VISIBLE;
        startDateTextView.setVisibility(divisibility);
    }

    private void saveChanges() {
        // Get the updated title and description from the EditText fields
        String updatedTitle = titleEditText.getText().toString().trim();
        String updatedDescription = descriptionEditText.getText().toString().trim();

        // Update the task object
        task.setTitle(updatedTitle);
        task.setDescription(updatedDescription);

        // TODO: Save the updated task to the database or SharedPreferences
        changeTask(task.getTaskId(), task.getTitle(), task.getDescription(), task.getDateTime(), this);
    }

    private void deleteTask() {
        // TODO: Delete the task from the database or SharedPreferences
        SPHelper.deleteTask(task.getTaskId(), task.getGroupId(), this);
    }
}