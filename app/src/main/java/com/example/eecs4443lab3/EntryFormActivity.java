package com.example.eecs4443lab3;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class EntryFormActivity extends AppCompatActivity {

    // Variables are established to be linked to the associated
    // EditText, TextView, Button, and CheckBox objects in the xml file activity_entry_form.xml

    EditText taskTitle;
    TextView taskDeadline;
    EditText taskDescription;
    Button cancelButton;
    Button createTaskButton;
    CheckBox demoCheckBox;

    // Within onCreate, the content is set to the activity_entry_form.xml file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entry_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.entryFormMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // The variables defined at the top are linked to the taskTitle EditText,
        // taskDeadline TextView, taskDescription EditText, cancelButton Button,
        // createTaskButton Button, and demoCheckbox Buttons found in the activity_entry_form.xml
        taskTitle = findViewById(R.id.taskTitle);
        taskDeadline = findViewById(R.id.taskDeadline);
        taskDescription = findViewById(R.id.taskDescription);
        cancelButton = findViewById(R.id.cancelButton);
        createTaskButton = findViewById(R.id.createTaskButton);
        demoCheckBox = findViewById(R.id.demoCheckBox);






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
            DatePickerDialog dialog = new DatePickerDialog(EntryFormActivity.this, (datePicker, yyyy, mm, dd) -> {

                // Strings dayFormat and monthFormat are created for the sake of always displaying day and month in format
                // dd and mm, even when their values are less than 2 digits
                String dayFormat;
                String monthFormat;

                // The date is checked to make sure a date earlier than today is not entered
                // ALEXA - Date Validation

                // If the day of the month selected is less than 10 (under 2 digits), the formatted day will be displayed with a
                // preceeding 0
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
            taskDeadline.setTextColor(858585);
            taskDescription.setText("");

            // Cancel then takes the user back to the Main Activity
            Intent cancelIntent = new Intent(EntryFormActivity.this, MainActivity.class);
            startActivity(cancelIntent);
        });

        // If the createTaskButton is clicked, a few things occur. They are all defined in sections below.
        createTaskButton.setOnClickListener(v -> {

            // In the case that the demo Checkbox is checked, the information from the taskTitle, taskDeadline
            // and taskDescription are stored in SQLite (local database)

            if (demoCheckBox.isChecked())
            {
                // SHERAZ - SQLITE IMPLEMENTATION AND LINKING TO TASKTITLE, TASKDEADLINE, TASKDESCRIPTION
            }

            // In the case that the demo Checkbox is not checked, the default storage method for the task information
            // will be kept in SharedPreferences
            else {
                // SHERAZ - SHAREDPREFERENCES AND LINKING TO TASKTITLE, TASKDEADLINE, TASKDESCRIPTION
            }

            // After the task has been stored, the information must be cleared from taskTitle, taskDeadline, and taskDescription
            // The colour is also reset for the taskDeadline TextView. This code is equivalent to that in the cancelButton
            taskTitle.setText("");
            taskDeadline.setText("MM/DD/YYYY");
            taskDeadline.setTextColor(858585);
            taskDescription.setText("");

            // A feedback message is provided to the user. The entry form is not exited out of until the cancelButton is clicked,
            // so no Intent is created to return to MainActivity
            Toast.makeText(EntryFormActivity.this, "Task created successfully!", 3);

        });
    }
}
