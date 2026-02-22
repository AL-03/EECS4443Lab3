// Originally written by Sheraz
package com.example.eecs4443lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    // Db name and version
    private static final String DATABASE_NAME = "TASKS.DB";
    private static final int DATABASE_VERSION  = 1;

    // Table name
    public static final String TABLE_NAME = "MY_TASKS";

    // Table columns
    public static final String COLUMN_TITLE = "task_title";
    public static final String COLUMN_DEADLINE= "task_deadline";
    public static final String COLUMN_DESCRIPTION = "task_description";
    public static final String COLUMN_STOREDINSQLITE = "task_storedinsqlite";
    public static final String COLUMN_ID = "_id";

    // Gets the context, db, an optional cursor factory, and an int representing the db schema version
    public SQLiteDatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates a new db to populate with tables and data
    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_DEADLINE + " TEXT, "  + COLUMN_DESCRIPTION +  " TEXT, " + COLUMN_STOREDINSQLITE + " TEXT);";
        db.execSQL(query);
    }

    // Upgrades schema version
    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}