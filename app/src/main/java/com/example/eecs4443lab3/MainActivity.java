package com.example.eecs4443lab3;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// EDIT: Temporary list
import java.util.ArrayList;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    // Get addTask button
    Button addTaskButton;

    // Declare RecyclerView and adapter variables
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    SharedPreferences sharedPrefs;

    // EDIT: Declaring an arrayList of Item objects, which will have 15 hardcoded Item objects added to it
    ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Access addTask button from activity_main.xml
        addTaskButton = findViewById(R.id.addTask);
        // When addTask clicked, switch to the AddTask screen
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EntryFormActivity.class);
                startActivity(intent);
            }
        });

        // Load data from SharedPreferences
        taskList = loadTasks();

        // Locate the RecyclerView object from activity_main.xml
        recyclerView = findViewById(R.id.recyclerView);
        // Create adapter to connect RecyclerView to data
        adapter = new RecyclerViewAdapter(this, taskList);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Create and set LinearLayoutManager for the RecyclerView to show list items vertically and not in reverse order
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    // Load tasks from SharedPreferences into ArrayList
    private ArrayList<Task> loadTasks() {
        taskList.clear();
        sharedPrefs = getSharedPreferences("TaskInformation", MODE_PRIVATE);

        for (int i = 0; i < sharedPrefs.getInt("taskCount", 0); i++) {
            String title = sharedPrefs.getString("title" + i, "Task not found");
            String deadline = sharedPrefs.getString("deadline" + i, "Deadline not found");
            String desc = sharedPrefs.getString("description" + i, "Description not found");

            if (title != null && deadline != null && desc != null) {
                taskList.add(new Task(title, deadline, desc));
            }
        }

        return taskList;
    }
}



