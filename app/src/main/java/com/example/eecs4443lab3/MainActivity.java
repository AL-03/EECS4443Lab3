package com.example.eecs4443lab3;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.SharedPreferences;
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
        // EDIT: When addTask clicked, switch to the AddTask screen
//        addTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, EntryFormActivity.class);
//                startActivity(intent);
//            }
//        });

        // Locate the RecyclerView object from activity_main.xml
        recyclerView = findViewById(R.id.recyclerView);
        // Create adapter to connect RecyclerView to data
        adapter = new RecyclerViewAdapter(this, taskList);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Create and set LinearLayoutManager for the RecyclerView to show list items vertically and not in reverse order
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // EDIT: Hard-coded list
        taskList.add(new Task("task1", "deadline1", "note1"));
        taskList.add(new Task("task2", "deadline2", "note2"));
        taskList.add(new Task("task3", "deadline3", "note3"));
        taskList.add(new Task("task4", "deadline4", "note4"));
        taskList.add(new Task("task5", "deadline5", "note5"));
        taskList.add(new Task("task6", "deadline6", "note6"));
        taskList.add(new Task("task7", "deadline7", "note7"));
        taskList.add(new Task("task8", "deadline8", "note8"));

        // Notify adapter that arraylist changed
        adapter.notifyDataSetChanged();
    }
}



