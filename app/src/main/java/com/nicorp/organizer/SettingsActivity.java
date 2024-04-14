package com.nicorp.organizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private static final String THEME_PREF = "theme_pref";
    private static final String THEME_LIGHT = "light";
    private static final String THEME_DARK = "dark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        Spinner spinner = findViewById(R.id.themeSpinner);
        ConstraintLayout aboutLayout = findViewById(R.id.aboutLayout);

        // Set click listener for About section
        aboutLayout.setOnClickListener(v -> {
            // create upper window with information about the app
            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("О приложении");
            builder.setMessage("Версия 1.0.0");
            builder.show();
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> list = new ArrayList<>();
        list.add("Light");
        list.add("Dark");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        // Set the adapter
        spinner.setAdapter(adapter);

        // Load saved theme
        SharedPreferences preferences = getSharedPreferences("Themes", MODE_PRIVATE);
        String savedTheme = preferences.getString(THEME_PREF, THEME_LIGHT); // Default to Light theme
        if (savedTheme.equals(THEME_LIGHT)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            spinner.setSelection(0);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            spinner.setSelection(1);
        }

        // Change app theme based on user selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveTheme(THEME_LIGHT);
                } else if (position == 1) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveTheme(THEME_DARK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void saveTheme(String theme) {
        SharedPreferences preferences = getSharedPreferences("Themes", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(THEME_PREF, theme);
        editor.apply();
    }
}
