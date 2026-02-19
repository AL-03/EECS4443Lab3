package com.example.eecs4443lab3;

import android.os.Bundle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
//import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    // Variables are established to be linked to the associated
    // EditText, TextView, Button, and CheckBox objects in the xml file activity_edit_task.xml

    EditText taskTitle;
    TextView taskDeadline;
    EditText taskDescription;
    Button cancelButton;
    Button saveButton;
    int pos;
    SharedPreferences sharedPrefs;

    // Within onCreate, the content is set to the activity_entry_form.xml file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editTaskMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gets the shared preferences
        sharedPrefs=getSharedPreferences("TaskInformation", MODE_PRIVATE);

        // The variables defined at the top are linked to the taskTitle EditText,
        // taskDeadline TextView, taskDescription EditText, cancelButton Button, and
        // saveButton Button found in the activity_entry_form.xml
        taskTitle = findViewById(R.id.taskTitle);
        taskDeadline = findViewById(R.id.taskDeadline);
        taskDescription = findViewById(R.id.taskDescription);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);

        // Set display text to be current values
        pos = getIntent().getIntExtra("pos", -1);
        taskTitle.setText(sharedPrefs.getString("title" + pos, ""));
        taskDeadline.setText(sharedPrefs.getString("deadline" + pos, ""));
        taskDescription.setText(sharedPrefs.getString("description" + pos, ""));

        // An onClickListener is created for the TextView such that users can edit the date after clicking anywhere within the field
        /*
        The onClick method fetches an instance of a calendar object, fetches the year, month, and day values from the
        object, and creates an associated DatePickerDialog to assign what the user selects from the Calendar UI to
        these values, displaying them back to the user. The comments below break up this functionality into sections.
         */
        taskDeadline.setOnClickListener(view -> {
            // A Calendar object deadlineCalendar is displayed when the user clicks the taskDeadline TextView
            Calendar deadlineCalendar = Calendar.getInstance();

            // ints for the year, month, and day components of the date are fetched from built-in Calendar attributes
            // YEAR, MONTH, and DAY_OF_MONTH
            int year = deadlineCalendar.get(Calendar.YEAR);
            int month = deadlineCalendar.get(Calendar.MONTH);
            int day = deadlineCalendar.get(Calendar.DAY_OF_MONTH);

            // A new DatePickerDialog named dialog is created, taking as its context the present Java file, and establishing a new
            // OnDateSetListener to allow the selected date to be displayed back in the taskDeadline TextView
            // onDateSet gets a datePicker object, and year, month, and day integers as its parameters
            // at the end of fhe format setting, the DatePickerDialog also takes the year, month, and day integers which have
            // been linked to Calendar.YEAR, Calendar.MONTH, and Calendar.DAY_OF_MONTH respectively
            DatePickerDialog dialog = new DatePickerDialog(EditTaskActivity.this, (datePicker, yyyy, mm, dd) -> {

                // Strings dayFormat and monthFormat are created for the sake of always displaying day and month in format
                // dd and mm, even when their values are less than 2 digits
                String dayFormat;
                String monthFormat;

                // If the day of the month selected is less than 10 (under 2 digits), the formatted day will be displayed with a
                // preceding 0
                if (dd < 10) {
                    dayFormat = "0" + dd;
                }
                // If the day of the month is 2 digits, the dayFormat is simply the string value of dd
                else {
                    dayFormat = String.valueOf(dd);
                }

                // The month taken from onDateSet is provided as values 0 - 11, which is inaccurate to the well-known 1 - 12
                // used to specify the months. As such, 1 is always added to mm, adjusting it to display the appropriate month
                mm = mm + 1;

                // If the month is less than 10 (under 2 digits), the formatted month will be displayed with a preceeding 0
                if (mm < 10) {
                    monthFormat = "0" + mm;
                }
                // If the month is 2 digits, the monthFormat is simply the string value of mm
                else {
                    monthFormat = String.valueOf(mm);
                }

                /*
                This colour setting has an if statement to ensure the textColor is changed from gray to black only once.
                Essentially, if the text of taskDeadline is equal to "MM/DD/YYYY" (which only occurs in the case that a
                date hasn't been set yet, the colour will be reset to BLACK once a date has been set. Otherwise, the text
                color should already be black
                */
                if (taskDeadline.getText().equals("MM/DD/YYYY")) {
                    taskDeadline.setTextColor(Color.BLACK);
                }

                // A formatted message is created to set the text of taskDeadline with the new selected date, ordered by
                // MM/DD/YYYY
                taskDeadline.setText(MessageFormat.format("{0}/{1}/{2}", monthFormat, dayFormat, String.valueOf(yyyy)));

            }, year, month, day);

            // Sets a minimum date of today so users cannot choose a past date
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());

            // the onClick ends by showing the new set date with the DatePickerDialog.show() method.
            dialog.show();
        });

        // an onClickListener is set for the cancel button to return to the main
        // activity and clear the fields
        cancelButton.setOnClickListener(v -> {

            // When cancel is clicked, the text fields are reset
            // to be either blank for title and description, or
            // the format string with gray color for the task deadline
            taskTitle.setText("");
            taskDeadline.setText("MM/DD/YYYY");
            taskDeadline.setTextColor(Color.GRAY);
            taskDescription.setText("");

            // Cancel then takes the user back to the Main Activity
            Intent cancelIntent = new Intent(EditTaskActivity.this, MainActivity.class);
            startActivity(cancelIntent);
        });

        // If the saveButton is clicked, a few things occur. They are all defined in sections below.
        saveButton.setOnClickListener(v -> {
            String title=taskTitle.getText().toString().trim();
            String deadline=taskDeadline.getText().toString().trim();

            boolean isValid=true;

            //check to ensure user entered task name and deadline
            if(title.isEmpty()){
                Toast.makeText(EditTaskActivity.this, "Please enter a task title.", Toast.LENGTH_SHORT).show();
                isValid=false;
            }
            //user must choose a deadline date
            if(deadline.equals("MM/DD/YYYY")){
                Toast.makeText(EditTaskActivity.this, "Please choose a task deadline.", Toast.LENGTH_SHORT).show();
                isValid=false;
            }
            //if user input is not valid, do not continue
            if(!isValid){
                return;
            }

            // In the case that the demo Checkbox is not checked, the default storage method for the task information
            // will be kept in SharedPreferences

            // Updates the key-value pairs in SharedPreferences
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("title" + pos, taskTitle.getText().toString());
            editor.putString("deadline" + pos, taskDeadline.getText().toString());
            editor.putString("description" + pos, taskDescription.getText().toString());

            // Applies the changes to the file
            editor.apply();

            // After the task has been stored, the information must be cleared from taskTitle, taskDeadline, and taskDescription
            // The colour is also reset for the taskDeadline TextView. This code is equivalent to that in the cancelButton
            taskTitle.setText("");
            taskDeadline.setText("MM/DD/YYYY");
            taskDeadline.setTextColor(Color.GRAY);
            taskDescription.setText("");

            // A feedback message is provided to the user. The entry form is not exited out of until the cancelButton is clicked,
            // so no Intent is created to return to MainActivity
            Toast.makeText(EditTaskActivity.this, "Task created successfully!", Toast.LENGTH_SHORT).show();

            // Destroys the activity and takes user back to MainActivity
            finish();
        });
    }
}