package com.nicorp.organizer;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.LongFunction;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

public class AddTaskActivity extends AppCompatActivity implements ColorPickerDialogListener {

    private EditText titleEditText, descriptionEditText;
    private View colorItem;
    private TextView startDateTextView, startTimeTextView;
    private ImageView cancelButton, addButton;
    private Calendar calendar = Calendar.getInstance();
    private Task task;
    private int TaskHourOfDay, TaskMinute, TaskDay;
    private int selectedColor = Color.WHITE; // Default color
    private CheckBox checkboxSunday, checkboxMonday, checkboxTuesday, checkboxWednesday, checkboxThursday, checkboxFriday, checkboxSaturday;
    private RadioGroup radioGroup;
    private RadioButton radioButtonOnce, radioButtonRecurring;
    private ScrollView recurringOptionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        colorItem = findViewById(R.id.colorItem);
        startDateTextView = findViewById(R.id.startDateTextView);
        startTimeTextView = findViewById(R.id.startTimeTextView);
        cancelButton = findViewById(R.id.cancelButton);
        addButton = findViewById(R.id.addButton);
        recurringOptionsLayout = findViewById(R.id.recurringOptionsLayout);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonOnce = findViewById(R.id.radioButtonOnce);
        radioButtonRecurring = findViewById(R.id.radioButtonRecurring);
        checkboxSunday = findViewById(R.id.checkboxSunday);
        checkboxMonday = findViewById(R.id.checkboxMonday);
        checkboxTuesday = findViewById(R.id.checkboxTuesday);
        checkboxWednesday = findViewById(R.id.checkboxWednesday);
        checkboxThursday = findViewById(R.id.checkboxThursday);
        checkboxFriday = findViewById(R.id.checkboxFriday);
        checkboxSaturday = findViewById(R.id.checkboxSaturday);

        updateDateTextView();
        updateTimeTextView();
        updateColorItemView();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonOnce) {
                setRecurringOptionsVisibility(false);
            } else if (checkedId == R.id.radioButtonRecurring) {
                setRecurringOptionsVisibility(true);
            }
        });

        // Set click listeners
        colorItem.setOnClickListener(v -> createColorPickerDialog(1));
        startDateTextView.setOnClickListener(v -> showDatePickerDialog());
        startTimeTextView.setOnClickListener(v -> showTimePickerDialog());
        cancelButton.setOnClickListener(v -> finish());
        addButton.setOnClickListener(v -> saveTask());
    }

    private void setRecurringOptionsVisibility(boolean visible) {
        int visibility = visible ? android.view.View.VISIBLE : android.view.View.GONE;
        recurringOptionsLayout.setVisibility(visibility);
        int divisibility = visible ?  android.view.View.GONE :android.view.View.VISIBLE;
        startDateTextView.setVisibility(divisibility);
    }


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

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                TaskHourOfDay = hourOfDay;
                TaskMinute = minute;

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

    private void updateDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        startDateTextView.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateTimeTextView() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        startTimeTextView.setText(timeFormat.format(calendar.getTime()));
    }

    private void updateColorItemView() {
        // create shape with 10dp radius and color = selectedColor
        GradientDrawable shape = new GradientDrawable();

        // Set radius for each corner
        shape.setCornerRadius(30);

        // Set background color
        shape.setColor(selectedColor);

        colorItem.setBackgroundDrawable(shape);
    }

    @SuppressLint("DefaultLocale")
    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        long dateTime = calendar.getTimeInMillis();// Get the selected date and time, e.g., from DatePickerDialog and TimePickerDialog

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioButtonOnce.isChecked()) {
            task = new Task(title, description, selectedColor, dateTime);
            Log.d("taskId", String.valueOf(task.getTaskId())); // taskId == 0 if task is new.getTaskId();
            Log.d("title", title);
            Log.d("description", description);
            saveSingleTask(task);
        } else if (radioButtonRecurring.isChecked()) {
            List<Integer> selectedDays = getIntegers();

            if (!selectedDays.isEmpty()) {
                for (Integer day : selectedDays) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(dateTime);
                    cal.set(Calendar.DAY_OF_WEEK, day);

                    TaskDay = day;

                    Log.d("title", title);
                    Log.d("description", description);

                    Task recurringTask = new Task(title, description, selectedColor, cal.getTimeInMillis(), true);
                    saveSingleTask(recurringTask);
                }
            } else {
                Toast.makeText(this, "Choose at least one day", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    private List<Integer> getIntegers() {
        List<Integer> selectedDays = new ArrayList<>();
        if (checkboxSunday.isChecked()) selectedDays.add(Calendar.SUNDAY);
        if (checkboxMonday.isChecked()) selectedDays.add(Calendar.MONDAY);
        if (checkboxTuesday.isChecked()) selectedDays.add(Calendar.TUESDAY);
        if (checkboxWednesday.isChecked()) selectedDays.add(Calendar.WEDNESDAY);
        if (checkboxThursday.isChecked()) selectedDays.add(Calendar.THURSDAY);
        if (checkboxFriday.isChecked()) selectedDays.add(Calendar.FRIDAY);
        if (checkboxSaturday.isChecked()) selectedDays.add(Calendar.SATURDAY);
        return selectedDays;
    }

    @SuppressLint({"ScheduleRecurringAlarm", "ScheduleExactAlarm"})
    private void saveSingleTask(Task SingleTask) {
        // Save the task to SharedPreferences
        SPHelper.saveTask(SingleTask, getBaseContext());

        // Set AlarmManager for notification at the specified time
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        // Pass additional data in Intent if needed
        intent.putExtra("task_title",SingleTask.getTitle());
        intent.putExtra("task_description", SingleTask.getDescription());
        intent.putExtra("task_start_time", TaskHourOfDay + ":" + TaskMinute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SingleTask.getTaskId(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (SingleTask.isRecurring()) {
            // Get the current day of the week
            Calendar now = Calendar.getInstance();
            // Get the current day of the week from task
            int currentDayOfWeek = TaskDay;

            // Настройка для определения ближайшего дня недели из выбранных
            int[] selectedDaysOfWeek = {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};
            int closestSelectedDayOfWeek = -1;
            for (int selectedDayOfWeek : selectedDaysOfWeek) {
                Log.d("AddTaskActivity", "Selected day of week: " + selectedDayOfWeek);
                Log.d("AddTaskActivity", "Current day of week: " + currentDayOfWeek);
                if (selectedDayOfWeek >= currentDayOfWeek) {
                    closestSelectedDayOfWeek = selectedDayOfWeek;
                    break;
                }
            }
            if (closestSelectedDayOfWeek == -1) {
                closestSelectedDayOfWeek = selectedDaysOfWeek[0]; // Если нет ближайшего, берем первый день недели
            }

            // Установка календаря на ближайший выбранный день недели
            Calendar nextAlarmDate = Calendar.getInstance();
            nextAlarmDate.set(Calendar.DAY_OF_WEEK, closestSelectedDayOfWeek);
            nextAlarmDate.set(Calendar.HOUR_OF_DAY, TaskHourOfDay); // Замените hour и minute на выбранные пользователем час и минуту
            nextAlarmDate.set(Calendar.MINUTE, TaskMinute);
            nextAlarmDate.set(Calendar.SECOND, 0);



            // Если ближайший день недели меньше текущего, добавляем 7 дней для получения следующего
            if (closestSelectedDayOfWeek < currentDayOfWeek) {
                Log.d("closestSelectedDayOfWeek", String.valueOf(closestSelectedDayOfWeek));
                Log.d("currentDayOfWeek", String.valueOf(currentDayOfWeek));
                Log.d("AddTaskActivity", "Adding 7 days to next alarm date");
                nextAlarmDate.add(Calendar.DATE, 7);
            }

            // Установка повторяющегося alarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            Log.d("AddTaskActivity with repeating", "Recurring task added: " + SingleTask.getTitle());
            // Log time of the next alarm
            Log.d("AddTaskActivity", "Next alarm at: " + nextAlarmDate.getTime());
        } else {
            // For one-time tasks, set single alarm
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getDateTime(), pendingIntent);
            Log.d("AddTaskActivity without repeating", "Recurring task added: " + SingleTask.getTitle());

        }

        finish(); // Close the activity after setting the alarm
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        selectedColor = color;
        updateColorItemView();
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}