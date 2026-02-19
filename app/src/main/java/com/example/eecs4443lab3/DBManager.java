package com.example.eecs4443lab3;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DBManager {
    private SQLiteDatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c)
    {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new SQLiteDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    void insert(String title, String deadline, String description)
    {
        ContentValues cv = new ContentValues();

        cv.put(SQLiteDatabaseHelper.COLUMN_TITLE, title);
        cv.put(SQLiteDatabaseHelper.COLUMN_DEADLINE, deadline);
        cv.put(SQLiteDatabaseHelper.COLUMN_DESCRIPTION, description);
        database.insert(SQLiteDatabaseHelper.TABLE_NAME, null, cv);

    }

    public void update(long _id, String title, String deadline, String description) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteDatabaseHelper.COLUMN_TITLE, title);
        cv.put(SQLiteDatabaseHelper.COLUMN_DEADLINE, deadline);
        cv.put(SQLiteDatabaseHelper.COLUMN_DESCRIPTION, description);
        database.update(SQLiteDatabaseHelper.TABLE_NAME, cv, SQLiteDatabaseHelper.COLUMN_ID + " = " + _id, null);
    }

    public void delete(long _id) {
        database.delete(SQLiteDatabaseHelper.TABLE_NAME, SQLiteDatabaseHelper.COLUMN_ID + "=" + _id, null);
    }
}
