package com.htc.avkrivonogov.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "mynotes.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_CATEGORIES = "categories";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "name";
    private static final String TABLE_CATEGORY_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CATEGORIES + " (" + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CATEGORY_NAME + " TEXT)";

    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_TASKS_NAME = "name";
    private static final String KEY_TASKS_CREATION = "creation";
    private static final String CATEGORY_ID = "category_id";
    private static final String TABLE_TASKS_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASKS + " (" + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASKS_NAME + " TEXT," + KEY_TASKS_CREATION + " INTEGER,"
            + CATEGORY_ID + " INTEGER, "
            + " FOREIGN KEY ("+CATEGORY_ID+") REFERENCES "+TABLE_CATEGORIES+"("+KEY_CATEGORY_ID+"));";

    private static final String TABLE_STEPS = "steps";
    //  private static final String KEY_STEP_ID = "step_id";
    private static final String KEY_STEP_NAME = "name";
    private static final String TASK_ID = "task_id";
    private static final String TABLE_STEP_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STEPS + " (" + KEY_STEP_NAME + " TEXT," + TASK_ID + " INTEGER,"
            + " FOREIGN KEY ("+TASK_ID+") REFERENCES "+TABLE_TASKS+"("+KEY_TASK_ID+"));";


    private SQLiteDatabase db;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_STEP_STRUCTURE);
        db.execSQL(TABLE_TASKS_STRUCTURE);
        db.execSQL(TABLE_CATEGORY_STRUCTURE);
        db.execSQL("INSERT INTO " + TABLE_TASKS +  " VALUES (1, 'qwerty', 12, 1)");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " VALUES (1, 'FAMILY')");
        db.execSQL("INSERT INTO " + TABLE_STEPS + " VALUES ('gogo', 1)");
        db.execSQL("INSERT INTO " + TABLE_STEPS + " VALUES ('nono', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
