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

        // If user clicks on an item, switch to DetailActivity screen and pass in taskName and deadline
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);

            intent.putExtra("taskName", taskList.get(position).getTaskName());
            intent.putExtra("deadline", taskList.get(position).getDeadline());

            v.getContext().startActivity(intent);
        });

        // If user does a long press, offer option to delete or update the task
        holder.itemView.setOnLongClickListener(v -> {
            showOptionsDialogue();
            return true;
        });
    }

    // Allows RecyclerView to understand number of tasks to display
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // EDIT: Include info needed to update & delete
    // Creates dialogue box after user long clicks a task
    private void showOptionsDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Task Options");

        String[] options = {"Update", "Delete"};

        builder.setItems(options, (dialog, which) -> {
            // EDIT: Show Update dialogue box if user clicked "Update"
            if (which == 0) {
                Toast.makeText(context, "Update task", Toast.LENGTH_SHORT).show();
            }
            // EDIT: Delete task if user clicked "Delete"
            else {
                Toast.makeText(context, "Delete task", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
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
