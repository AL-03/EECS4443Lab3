package com.example.eecs4443lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// EDIT: Temporary list
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    // EDIT: Temporary list
    ArrayList<Task> taskList = new ArrayList<>();

    // EDIT: Should take data from db, not arraylist
    public RecyclerViewAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    // Inflate the layout and actually create view of each task
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_layout, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    // Assign values to the views created in task_layout.xml
    // Based on RecyclerView's position (i.e. what's currently displayed on-screen)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.task.setText(taskList.get(position).getTaskName());
        holder.deadline.setText(taskList.get(position).getDeadline());
    }

    // Allows RecyclerView to understand number of tasks to display
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Store views from task_layout.xml as variables
        TextView task, deadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.task);
            deadline = itemView.findViewById(R.id.deadline);
        }
    }
}
