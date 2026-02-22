package com.example.eecs4443lab3;

// Defines Task object with a task name, deadline, and notes
public class Task {
    String id;
    String taskName;
    String deadline;
    String notes;
    // Indicates whether task is stored in SQLite or sharedPrefs
    boolean storedInSQLite;

    public Task(String id, String taskName, String deadline, String notes, boolean storedInSQLite) {
        this.id = id;
        this.taskName = taskName;
        this.deadline = deadline;
        this.notes = notes;
        this.storedInSQLite = storedInSQLite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean getStoredInSQLite() {
        return storedInSQLite;
    }

    public void setStoredInSQLite(boolean storedInSQLite) {
        this.storedInSQLite = storedInSQLite;
    }
}
