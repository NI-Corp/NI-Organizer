package com.nicorp.organizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }
        TabLayout daysTabLayout = findViewById(R.id.daysTabLayout);
        ViewPager2 tasksViewPager = findViewById(R.id.tasksViewPager);
        DaysPagerAdapter pagerAdapter = new DaysPagerAdapter(this);
        tasksViewPager.setAdapter(pagerAdapter);

        // Связываем TabLayout с ViewPager2
        new TabLayoutMediator(daysTabLayout, tasksViewPager,
                (tab, position) -> {
                    // Установка названий для вкладок, например: "Пн", "Вт", "Ср", ...
                    String[] dayNames = getResources().getStringArray(R.array.day_names);
                    tab.setText(dayNames[position]);
                }
        ).attach();

        ImageView addTaskButton = findViewById(R.id.addTaskButton);
        ImageView settingsButton = findViewById(R.id.settingsButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}