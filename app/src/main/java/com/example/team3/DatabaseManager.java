package com.example.team3;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context ctx) {
        dbHelper = DatabaseHelper.getInstance(ctx);
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Database not open. Call open() method first.");
        }
        return database;
    }

    public DatabaseManager open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void enroll(String userName, int time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_NAME, userName);
        values.put("time", time);

        long result = database.insertWithOnConflict(DatabaseHelper.DATABASE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // 에러 로그
            android.util.Log.e("DatabaseManager", "Failed to enroll user: " + userName);
        }
    }
}
