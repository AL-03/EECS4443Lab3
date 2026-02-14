package com.example.eecs4443lab3;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

public class TaskValidator {
    public static boolean validateTask(Context context, String title, long chosenDeadlineMillis){
       //user must enter a task title
        if(title==null || title.trim().isEmpty()){
            Toast.makeText(context, "Please enter a task title.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //user must choose a deadline date
        if(chosenDeadlineMillis==-1){
            Toast.makeText(context, "Please enter a task deadline.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //get today's date
        Calendar today=Calendar.getInstance();
        Calendar chosenDeadline=Calendar.getInstance();
        chosenDeadline.setTimeInMillis(chosenDeadlineMillis);
        //make sure deadline chosen is not in the past
        if(chosenDeadline.before(today)){
            Toast.makeText(context, "Deadline cannot be in the past.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;







    }
}
