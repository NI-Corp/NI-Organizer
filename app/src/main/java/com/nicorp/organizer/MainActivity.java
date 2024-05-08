package com.nicorp.organizer;

import static android.app.PendingIntent.getActivity;
import static android.service.notification.Condition.SCHEME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private static final String THEME_PREF = "theme_pref";
    private static final String THEME_LIGHT = "light";
    private static final String THEME_DARK = "dark";

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
        ConstraintLayout loadingPB = findViewById(R.id.loadingPB);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Initialize fade out animation
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1500);
        fadeOut.setDuration(1000);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeOut);

        // Start fade out animation
        loadingPB.startAnimation(animation);

        // Set a listener to be notified when the animation ends
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Not needed for this example
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Set the visibility of loadingPB to "gone" after the animation ends
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Not needed for this example
            }
        });

        // Load theme from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Themes", MODE_PRIVATE);
        String savedTheme = preferences.getString(THEME_PREF, THEME_LIGHT); // Default to Light theme
        if (savedTheme.equals(THEME_LIGHT)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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