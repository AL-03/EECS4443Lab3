package com.example.eecs4443lab3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    // Task list
    ArrayList<Task> taskList = new ArrayList<>();
    // Needed for long press function
    TaskListener listener;

    // EDIT: Should take data from db too
    public RecyclerViewAdapter(Context context, ArrayList<Task> taskList, TaskListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
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
        Task task = taskList.get(position);
        holder.task.setText(task.getTaskName());
        holder.deadline.setText(task.getDeadline());

        // If user clicks on an item, switch to DetailActivity screen and pass in taskName and deadline
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);

            intent.putExtra("taskName", task.getTaskName());
            intent.putExtra("deadline", task.getDeadline());
            intent.putExtra("desc", task.getNotes());

            v.getContext().startActivity(intent);
        });

        // If user does a long press, offer option to delete or update the task
        holder.itemView.setOnLongClickListener(v -> {
            listener.onTaskLongPressed(position);
            return true;
        });
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

    // Used to notify MainActivity when user has long-pressed on a task
    // MainActivity implements the logic for onTaskLongPressed for clear separation of concerns between UI and logic
    public interface TaskListener {
        void onTaskLongPressed(int pos);
    }
}
