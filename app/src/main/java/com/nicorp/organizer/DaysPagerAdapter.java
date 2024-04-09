package com.nicorp.organizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class DaysPagerAdapter extends FragmentStateAdapter {

    public DaysPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Поскольку дни недели в Calendar начинаются с воскресенья (1),
        // а позиции фрагментов начинаются с 0 (понедельник), делаем корректировку
        int dayOfWeek = (position + 2) % 7;
        if (dayOfWeek == 0) {
            dayOfWeek = 7; // Воскресенье (7)
        }
        return DayFragment.newInstance(dayOfWeek);
    }

    @Override
    public int getItemCount() {
        return 7; // 7 дней недели
    }
}