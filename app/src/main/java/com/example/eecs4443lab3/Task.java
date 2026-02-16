package com.example.eecs4443lab3;

// Defines Task object with a task name, deadline, and notes
public class Task {
    String taskName;
    String deadline;
    String notes;

    public Task(String taskName, String deadline, String notes) {
        this.taskName = taskName;
        this.deadline = deadline;
        this.notes = notes;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
