// Originally written by Sheraz
package com.example.eecs4443lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// Performs db CRUD operations
public class DBManager {
    private SQLiteDatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;
    // String list of columns in db
    String[] cols = new String[] {SQLiteDatabaseHelper.COLUMN_ID, SQLiteDatabaseHelper.COLUMN_TITLE, SQLiteDatabaseHelper.COLUMN_DEADLINE, SQLiteDatabaseHelper.COLUMN_DESCRIPTION, SQLiteDatabaseHelper.COLUMN_STOREDINSQLITE};

    public DBManager(Context c)
    {
        context = c;
    }

    // Opens db connection to allow for insertion/update/deletion of records
    public DBManager open() throws SQLException {
        dbHelper = new SQLiteDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    // Close db connection
    public void close() {
        dbHelper.close();
    }

    // Adds a new record to db
    void insert(String title, String deadline, String description, boolean storedInSQLite)
    {
        ContentValues cv = new ContentValues();

        cv.put(SQLiteDatabaseHelper.COLUMN_TITLE, title);
        cv.put(SQLiteDatabaseHelper.COLUMN_DEADLINE, deadline);
        cv.put(SQLiteDatabaseHelper.COLUMN_DESCRIPTION, description);
        cv.put(SQLiteDatabaseHelper.COLUMN_STOREDINSQLITE, storedInSQLite);
        database.insert(SQLiteDatabaseHelper.TABLE_NAME, null, cv);

    }

    // Fetches a record when given an ID
    public Cursor fetchTaskByID(long id) {
        // API format note: query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        // SQLiteDatabaseHelper.COLUMN_ID + " = ?" is the selection criteria, where "?" is a placeholder for the ID
        // We then supply the ID in the String list containing the ID passed into this function
        Cursor cursor = database.query(SQLiteDatabaseHelper.TABLE_NAME, cols, SQLiteDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        // If the query result isn't empty, move cursor to the result
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Fetches all records from db; equivalent of:
    // SELECT _id, title, deadline, description
    // FROM table_name;
    // The "null"s are to remove parameters like GroupBy from the query
    public Cursor fetch() {
        Cursor cursor = database.query(SQLiteDatabaseHelper.TABLE_NAME, cols, null, null, null, null, null);
        // If query result isn't empty, move cursor to 1st result
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Updates a specific record and returns an int indicating the number of rows affected in the db
    public int update(long _id, String title, String deadline, String description, boolean storedInSQLite) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteDatabaseHelper.COLUMN_TITLE, title);
        cv.put(SQLiteDatabaseHelper.COLUMN_DEADLINE, deadline);
        cv.put(SQLiteDatabaseHelper.COLUMN_DESCRIPTION, description);
        cv.put(SQLiteDatabaseHelper.COLUMN_STOREDINSQLITE, storedInSQLite);
        int i = database.update(SQLiteDatabaseHelper.TABLE_NAME, cv, SQLiteDatabaseHelper.COLUMN_ID + " = " + _id, null);
        return i;
    }

    // Deletes the record with the given ID
    public void delete(long _id) {
        database.delete(SQLiteDatabaseHelper.TABLE_NAME, SQLiteDatabaseHelper.COLUMN_ID + "=" + _id, null);
    }
}