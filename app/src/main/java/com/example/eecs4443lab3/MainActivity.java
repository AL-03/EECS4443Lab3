package com.example.eecs4443lab3;

import android.app.AlertDialog;
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
import android.widget.Toast;

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

        // Set up SharedPreferences
        sharedPrefs = getSharedPreferences("TaskInformation", MODE_PRIVATE);

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
        // Overrides methods needed for long-press functions
        adapter = new RecyclerViewAdapter(this, taskList, new RecyclerViewAdapter.TaskListener() {
            @Override
            public void onTaskLongPressed(int pos) {
                showOptionsDialogue(pos);
            }
        });
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Create and set LinearLayoutManager for the RecyclerView to show list items vertically and not in reverse order
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList = loadTasks();
        adapter.notifyDataSetChanged();
    }

    // Load tasks from SharedPreferences into ArrayList
    private ArrayList<Task> loadTasks() {
        taskList.clear();

        // Create a new Task object for each task in SharedPreferences and save it to taskList
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

    // EDIT: Include info needed to update & delete
    // Creates dialogue box after user long clicks a task
    private void showOptionsDialogue(int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Task Options");

        String[] options = {"Update", "Delete"};

        builder.setItems(options, (dialog, which) -> {
            // EDIT: Show Update dialogue box if user clicked "Update"
            if (which == 0) {
                // Go to EditTaskActivity
                Intent editIntent = new Intent(MainActivity.this, EditTaskActivity.class);
                // Pass RecyclerView position as we move to EditTaskActivity
                // Upon closing EditTaskActivity, onResume() will run and refresh the data
                editIntent.putExtra("pos", pos);
                startActivity(editIntent);
                Toast.makeText(this, "Updated task", Toast.LENGTH_SHORT).show();
            }
            // EDIT: Delete task if user clicked "Delete"
            else {
                deleteTask(pos);
                Toast.makeText(this, "Deleted task", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // Deletes task from SharedPreferences
    private void deleteTask(int pos) {
        // Allow editing of sharedPrefs
        SharedPreferences.Editor editor = sharedPrefs.edit();

        int taskCount = sharedPrefs.getInt("taskCount", 0);

        // Shift task names down by 1
        for (int i = pos; i < taskCount - 1; i++) {
            editor.putString("title" + i, sharedPrefs.getString("title" + (i + 1), ""));
            editor.putString("deadline" + i, sharedPrefs.getString("deadline" + (i + 1), ""));
            editor.putString("description" + i, sharedPrefs.getString("description" + (i + 1), ""));
        }

        // Remove the last item (duplicated, now that everything's been shifted)
        editor.remove("title" + (taskCount - 1));
        editor.remove("deadline" + (taskCount - 1));
        editor.remove("description" + (taskCount - 1));

        // Update taskCount in SharedPreferences
        editor.putInt("taskCount", taskCount - 1);

        // Apply SharedPreferences changes
        editor.apply();

        // Refresh UI
        taskList = loadTasks();
        adapter.notifyDataSetChanged();
    }
}



