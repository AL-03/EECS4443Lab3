package com.example.eecs4443lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

        // Connection string for the SQLite database file
       private Context context;
       private static final String DATABASE_NAME = "task.db";
       private static final int DATABASE_VERSION  = 1;

       public static final String TABLE_NAME = "my_tasks";
       public static final String COLUMN_TITLE = "task_title";
       public static final String COLUMN_DEADLINE= "task_deadline";
       public static final String COLUMN_DESCRIPTION = "task_description";
       public static final String COLUMN_ID = "_id";

        public SQLiteDatabaseHelper(@Nullable Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_DEADLINE + " TEXT, "  + COLUMN_DESCRIPTION + " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
