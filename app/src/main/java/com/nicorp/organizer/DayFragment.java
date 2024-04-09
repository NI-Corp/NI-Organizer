package com.nicorp.organizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DayFragment extends Fragment {

    private static final String ARG_DAY_OF_WEEK = "dayOfWeek";

    private int dayOfWeek;
    private RecyclerView tasksRecyclerView;

    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(int dayOfWeek) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OF_WEEK, dayOfWeek);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt(ARG_DAY_OF_WEEK);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Обновляем список задач
            loadTasks(dayOfWeek);
            swipeRefreshLayout.setRefreshing(false);
        });

        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTasks(dayOfWeek);

        return view;
    }

    private void loadTasks(int dayOfWeek) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Tasks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> tasks;
        if (json == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(json, type);
        }

        // Фильтрация задач по дню недели
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(task.getDateTime());
            int taskDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (taskDayOfWeek == dayOfWeek) {
                filteredTasks.add(task);
            }
        }

        TasksAdapter adapter = new TasksAdapter(filteredTasks);
        tasksRecyclerView.setAdapter(adapter);
    }
}