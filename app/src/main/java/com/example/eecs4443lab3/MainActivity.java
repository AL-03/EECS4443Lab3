package com.example.eecs4443lab3;

import android.content.SharedPreferences;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText editTitle, editDeadline, editNotes;
    private Button createTaskButton;
    private long chosenDeadlineMillis=-1;

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

        editTitle=findViewById(R.id.editTitle);
        editDeadline=findViewById(R.id.editDeadline);
        editNotes=findViewById(R.id.editNotes);
        createTaskButton=findViewById(R.id.createTaskButton);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar=Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            chosenDeadlineMillis=calendar.getTimeInMillis();
        });

        //create task button listner sends to submission handler to ensure proper validation
        createTaskButton.setOnClickListener(v -> submissionHandler());


    }
    private void submissionHandler(){
        String title=editTitle.getText().toString();
        //if title and deadline are not valid, do not submit the task
        if (!TaskValidator.validateTask(this,title,chosenDeadlineMillis)){
            return;
        }
    }
}



