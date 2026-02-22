package com.example.eecs4443lab3;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    // Get addTask button
    Button addTaskButton;

    // Declare RecyclerView and adapter variables
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    // Declare SharedPreferences variable
    SharedPreferences sharedPrefs;

    // Declare db-related variables
    private DBManager dbManager;
    private SimpleCursorAdapter cursorAdapter;

    // Declaring an arrayList of Task objects
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
            public void onTaskLongPressed(Task task) {
                showOptionsDialogue(task);
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

    // Load tasks from SharedPreferences and db into ArrayList
    private ArrayList<Task> loadTasks() {
        taskList.clear();

        // Create a new Task object for each task in SharedPreferences and save it to taskList
        for (int i = 0; i < sharedPrefs.getInt("taskCount", 0); i++) {
            String id = "task" + i;
            String title = sharedPrefs.getString("title" + i, "Task not found");
            String deadline = sharedPrefs.getString("deadline" + i, "Deadline not found");
            String desc = sharedPrefs.getString("description" + i, "Description not found");

            if (title != null && deadline != null && desc != null) {
                taskList.add(new Task(id, title, deadline, desc, false));
            }
        }

        // Set up DBManager and open the SQLite connection
        dbManager = new DBManager(this);
        dbManager.open();

        // Create a new Task object for each task in db
        Cursor cursor = dbManager.fetch();
        // Create a new Task for each task stored in db and save it to taskList, assuming the cursor isn't pointing to an empty list
        if (cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.COLUMN_ID)));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.COLUMN_TITLE));
                String deadline = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.COLUMN_DEADLINE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.COLUMN_DESCRIPTION));

                if (title != null && deadline != null && desc != null) {
                    taskList.add(new Task(id, title, deadline, desc, true));
                }
            }
            while (cursor.moveToNext());
        }

        // Close db connection
        cursor.close();
        dbManager.close();

        return taskList;
    }

    // Creates dialogue box after user long clicks a task
    private void showOptionsDialogue(Task task) {
        // Dialogue box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Task Options");

        String[] options = {"Update", "Delete"};

        builder.setItems(options, (dialog, which) -> {
            // Show Update dialogue box if user clicked "Update"
            if (which == 0) {
                // Go to EditTaskActivity
                Intent editIntent = new Intent(MainActivity.this, EditTaskActivity.class);
                // Pass RecyclerView position as we move to EditTaskActivity
                // Upon closing EditTaskActivity, onResume() will run and refresh the data
                // Note that we can't directly pass a Task object in, so only an indication of the data source and ID
                editIntent.putExtra("storedInSQLite", task.getStoredInSQLite());
                editIntent.putExtra("taskID", task.getId());
                startActivity(editIntent);
            }
            // Delete task if user clicked "Delete"
            else {
                deleteTask(task);
                Toast.makeText(this, "Deleted task", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // Deletes task
    private void deleteTask(Task task) {
        // Check whether the task was stored in SharedPreferences or SQLite
        if (task.getStoredInSQLite()) {
            // Deletes task from SQLite db
            // Set up DBManager and open the SQLite connection
            dbManager = new DBManager(this);
            dbManager.open();

            // Delete task with the given ID
            dbManager.delete(Long.parseLong(task.getId()));

            // Close the SQLite connection
            dbManager.close();
        }
        else {
            // Deletes task from SharedPreferences
            // Allow editing of sharedPrefs
            SharedPreferences.Editor editor = sharedPrefs.edit();

            int taskCount = sharedPrefs.getInt("taskCount", 0);
            // Task's "position" in sharedPrefs
            // We need it as an integer, but it's the final char of the task's ID
            // In order to use parseInt, we need to convert that char back to String
            int spPos = Integer.parseInt(String.valueOf(task.getId().charAt(task.getId().length() - 1)));

            // Shift task names down by 1
            for (int i = spPos; i < taskCount - 1; i++) {
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
        }

        // Refresh UI
        taskList = loadTasks();
        adapter.notifyDataSetChanged();
    }
}



