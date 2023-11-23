package com.example.team3;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance; // Singleton 인스턴스
    private SQLiteDatabase database;

    static final String DATABASE_NAME = "cats_adventure.DB";
    static final int DATABASE_VERSION = 6; 


    static final String DATABASE_TABLE = "집사";
    static final String USER_NAME = "집사_name";

    private static final String CREATE_DB_QUERY =
            "CREATE TABLE " + DATABASE_TABLE + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_NAME + " TEXT NOT NULL UNIQUE, " +
                    "time INTEGER" + // (time 컬럼이 있다고 가정)
                    ");";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton 패턴으로 인스턴스 반환
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Database not open. Call open() method first.");
        }
        return database;
    }

    public void open() throws SQLException {
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
    public void deleteRank(int userId) {
        try {
            getDatabase().delete(DATABASE_TABLE, "_id=?", new String[]{String.valueOf(userId)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
