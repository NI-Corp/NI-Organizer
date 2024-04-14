package com.nicorp.organizer;

import static com.nicorp.organizer.SPHelper.changeTask;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity implements ColorPickerDialogListener {

    private Task task;
    private TextView titleEditText, descriptionEditText, startTimeTextView, startDateTextView;
    private ImageView saveButton, cancelButton, deleteButton;
    private CheckBox checkboxSunday, checkboxMonday, checkboxTuesday, checkboxWednesday, checkboxThursday, checkboxFriday, checkboxSaturday;
    private RadioGroup radioGroup;
    private ScrollView recurringOptionsLayout;
    private ConstraintLayout colorItem;
    private Calendar calendar = Calendar.getInstance();
    private int taskHourOfDay, taskMinute, taskDay;
    private CheckBox[] dayCheckBoxes;
    private RadioButton radioButtonOnce, radioButtonRecurring;
    private int selectedColor;


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
                colorItem = findViewById(R.id.colorItem);
                dayCheckBoxes = new CheckBox[]{
                        findViewById(R.id.checkboxSunday),
                        findViewById(R.id.checkboxMonday),
                        findViewById(R.id.checkboxTuesday),
                        findViewById(R.id.checkboxWednesday),
                        findViewById(R.id.checkboxThursday),
                        findViewById(R.id.checkboxFriday),
                        findViewById(R.id.checkboxSaturday)
                };
                radioButtonOnce = findViewById(R.id.radioButtonOnce);
                radioButtonRecurring = findViewById(R.id.radioButtonRecurring);
                radioGroup = findViewById(R.id.radioGroup);

                deleteButton.setOnClickListener(v -> {
                    deleteTask();
                });
                colorItem.setOnClickListener(v -> createColorPickerDialog(1));
                startDateTextView.setOnClickListener(v -> showDatePickerDialog());
                startTimeTextView.setOnClickListener(v -> showTimePickerDialog());

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
                selectedColor = task.getColor();

                // RadioGroup listener for recurring options
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    setRecurringOptionsVisibility(checkedId == R.id.radioButtonRecurring);
                });

                // get Time from task.getDateTime() (long)
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String time = sdf.format(task.getDateTime());
                // set time to textView
                startTimeTextView.setText(time);

                if (!task.isRecurring()) {
                    setRecurringOptionsVisibility(false);
                    radioButtonOnce.setChecked(true);
                    radioButtonRecurring.setChecked(false);

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(task.getDateTime());

                    // get date from task.getDateTime() (long)
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime());
                    startDateTextView.setText(date);
                } else {
                    // find all tasks with same group id by SPHelper
                    ArrayList<Task> tasksWithSameGroupId = SPHelper.getTasksWithSameGroupId(task.getGroupId(), this);
                    radioButtonOnce.setChecked(false);
                    radioButtonRecurring.setChecked(true);
                    // Log names of all tasks with same group id
                    for (Task taskWithSameGroupId : tasksWithSameGroupId) {
                        Log.d("taskWithSameGroupId", String.valueOf(taskWithSameGroupId.getDateTime()));
                    }

                    // set visibility
                    setRecurringOptionsVisibility(true);

                    // set checkboxes for recurring tasks
                    for (Task taskWithSameGroupId : tasksWithSameGroupId) {
                        if (taskWithSameGroupId.getTaskId() == task.getTaskId()) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(task.getDateTime());
                            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek == Calendar.SUNDAY) {
                                checkboxSunday.setChecked(true);
                            }  if (dayOfWeek == Calendar.MONDAY) {
                                checkboxMonday.setChecked(true);
                            }  if (dayOfWeek == Calendar.TUESDAY ) {
                                checkboxTuesday.setChecked(true);
                            }  if (dayOfWeek == Calendar.WEDNESDAY ) {
                                checkboxWednesday.setChecked(true);
                            }  if (dayOfWeek == Calendar.THURSDAY ) {
                                checkboxThursday.setChecked(true);
                            }  if (dayOfWeek == Calendar.FRIDAY ) {
                                checkboxFriday.setChecked(true);
                            }  if (dayOfWeek == Calendar.SATURDAY ) {
                                checkboxSaturday.setChecked(true);
                            }
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
                        deleteTaskWithoutDialog();
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

//    @SuppressLint("ScheduleExactAlarm")
//    private void saveChanges() {
//        // TODO: Save the changes to the task
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.putExtra("task_title", task.getTitle());
//        intent.putExtra("task_description", task.getDescription());
//        intent.putExtra("task_start_time", taskHourOfDay + ":" + taskMinute);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getTaskId(), intent, PendingIntent.FLAG_IMMUTABLE);
//
//        if (task.isRecurring()) {
//            // Set repeating alarm for recurring task
//            Calendar now = Calendar.getInstance();
//            int currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
//            int closestSelectedDayOfWeek = getClosestSelectedDayOfWeek(currentDayOfWeek);
//
//            Calendar nextAlarmDate = Calendar.getInstance();
//            nextAlarmDate.set(Calendar.DAY_OF_WEEK, closestSelectedDayOfWeek);
//            nextAlarmDate.set(Calendar.HOUR_OF_DAY, taskHourOfDay);
//            nextAlarmDate.set(Calendar.MINUTE, taskMinute);
//            nextAlarmDate.set(Calendar.SECOND, 0);
//
//            if (closestSelectedDayOfWeek < currentDayOfWeek) {
//                nextAlarmDate.add(Calendar.DATE, 7);
//            }
//
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
//            Log.d("AddTaskActivity", "Recurring task added: " + task.getTitle());
//            Log.d("AddTaskActivity", "Next alarm at: " + nextAlarmDate.getTime());
//        } else {
//            // Set single alarm for one-time task
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getDateTime(), pendingIntent);
//            Log.d("AddTaskActivity", "One-time task added: " + task.getTitle());
//        }
//
//        // TODO: Save the updated task to the database or SharedPreferences
//        changeTask(task.getTaskId(), task.getTitle(), task.getDescription(), task.getDateTime(), this);
//    }

    // Save task data (single or recurring)
    @SuppressLint({"ScheduleRecurringAlarm", "ScheduleExactAlarm"})
    private void saveChanges() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        long dateTime = calendar.getTimeInMillis();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Count how many groups are in the database
        int groupCount = SPHelper.getGroupCount(getBaseContext()); // Count how many groups are in the database

        if (radioButtonOnce.isChecked()) {
            task = new Task(title, description, selectedColor, dateTime);
            saveSingleChanges(task, 0);
        } else if (radioButtonRecurring.isChecked()) {
            List<Integer> selectedDays = getSelectedDays();
            if (!selectedDays.isEmpty()) {
                for (Integer day : selectedDays) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(dateTime);
                    cal.set(Calendar.DAY_OF_WEEK, day);
                    taskDay = day;
                    Task recurringTask = new Task(title, description, selectedColor, cal.getTimeInMillis(), true, 1);
                    saveSingleChanges(recurringTask, groupCount+1);
                }
            } else {
                Toast.makeText(this, "Choose at least one day", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Save single task and set alarm
    @SuppressLint({"ScheduleRecurringAlarm", "ScheduleExactAlarm"})
    private void saveSingleChanges(Task singleTask, int groupId) {

        singleTask.setGroupId(groupId);

        SPHelper.saveTask(singleTask, getBaseContext()); // Save to SharedPreferences

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("task_title", singleTask.getTitle());
        intent.putExtra("task_description", singleTask.getDescription());
        intent.putExtra("task_start_time", taskHourOfDay + ":" + taskMinute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, singleTask.getTaskId(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (singleTask.isRecurring()) {
            // Set repeating alarm for recurring task
            Calendar now = Calendar.getInstance();
            int currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
            int closestSelectedDayOfWeek = getClosestSelectedDayOfWeek(currentDayOfWeek);

            Calendar nextAlarmDate = Calendar.getInstance();
            nextAlarmDate.set(Calendar.DAY_OF_WEEK, closestSelectedDayOfWeek);
            nextAlarmDate.set(Calendar.HOUR_OF_DAY, taskHourOfDay);
            nextAlarmDate.set(Calendar.MINUTE, taskMinute);
            nextAlarmDate.set(Calendar.SECOND, 0);

            if (closestSelectedDayOfWeek < currentDayOfWeek) {
                nextAlarmDate.add(Calendar.DATE, 7);
            }

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            Log.d("AddTaskActivity", "Recurring task added: " + singleTask.getTitle());
            Log.d("AddTaskActivity", "Next alarm at: " + nextAlarmDate.getTime());
        } else {
            // Set single alarm for one-time task
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, singleTask.getDateTime(), pendingIntent);
            Log.d("AddTaskActivity", "One-time task added: " + singleTask.getTitle());
        }
        finish(); // Close activity
    }

    private void deleteTaskWithoutDialog() {
        // TODO: Delete the task from the database or SharedPreferences
        SPHelper.deleteTaskWithoutDialog(task.getTaskId(), task.getGroupId(), this);
    }

    private void deleteTask() {
        // TODO: Delete the task from the database or SharedPreferences
        SPHelper.deleteTask(task.getTaskId(), task.getGroupId(), this);
    }

    // Create ColorPickerDialog
    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this);
    }

    // Show DatePickerDialog
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTextView();
            }
        };
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Show TimePickerDialog
    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                taskHourOfDay = hourOfDay;
                taskMinute = minute;
                updateTimeTextView();
            }
        };
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                timeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    // Update TextView with selected date
    private void updateDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        startDateTextView.setText(dateFormat.format(calendar.getTime()));
    }

    // Update TextView with selected time
    private void updateTimeTextView() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        startTimeTextView.setText(timeFormat.format(calendar.getTime()));
    }

    // Helper method to find the closest selected day of the week
    private int getClosestSelectedDayOfWeek(int currentDayOfWeek) {
        int closestSelectedDayOfWeek = -1;
        for (int selectedDay : getSelectedDays()) {
            if (selectedDay >= currentDayOfWeek) {
                closestSelectedDayOfWeek = selectedDay;
                break;
            }
        }
        if (closestSelectedDayOfWeek == -1) {
            closestSelectedDayOfWeek = getSelectedDays().get(0); // If none found, use the first selected day
        }
        return closestSelectedDayOfWeek;
    }

    // Get selected days of the week as a List<Integer>
    @NonNull
    private List<Integer> getSelectedDays() {
        List<Integer> selectedDays = new ArrayList<>();
        for (int i = 0; i < dayCheckBoxes.length; i++) {
            if (dayCheckBoxes[i].isChecked()) {
                selectedDays.add(i + 1); // Add day of week (1 = Sunday, 7 = Saturday)
            }
        }
        return selectedDays;
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        selectedColor = color;
        updateColorItemView();
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        // Do nothing
    }
    // Update colorItemView with selected color
    private void updateColorItemView() {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(30);
        shape.setColor(selectedColor);
        colorItem.setBackgroundDrawable(shape);
    }

}