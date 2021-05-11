package com.htc.avkrivonogov.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "notes.db";
    public static final int DB_VERSION = 1;

    //Table TaskList
    public static final String TABLE_TASK_LIST = "categories";
    public static final String KEY_TASK_LIST_ID = "category_id";
    public static final String KEY_TASK_LIST_NAME = "name";
    public static final String TABLE_TASK_LIST_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASK_LIST + " ("
            + KEY_TASK_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASK_LIST_NAME + " TEXT NOT NULL)";

    //Table Tasks
    public static final String TABLE_TASKS = "tasks";
    public static final String KEY_TASK_ID = "task_id";
    public static final String KEY_TASK_NAME = "name";
    public static final String KEY_TASK_DESCRIPTION = "description";
    public static final String KEY_TASK_IMAGE = "image";
    public static final String KEY_TASK_CREATION = "creation";
    public static final String KEY_TASK_COMPLETE_DATE = "complete_date";
    public static final String KEY_TASK_REMINDER = "reminder";
    public static final String KEY_TASK_STATUS = "complete_status";
    public static final String CATEGORY_ID = "category_id";
    public static final String TABLE_TASKS_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASKS + " ("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASK_NAME + " TEXT NOT NULL, "
            + KEY_TASK_DESCRIPTION + " TEXT, "
            + KEY_TASK_IMAGE + " BLOB, "
            + KEY_TASK_CREATION + " TEXT, "
            + KEY_TASK_COMPLETE_DATE + " TEXT, "
            + KEY_TASK_REMINDER + " TEXT, "
            + KEY_TASK_STATUS + " INTEGER DEFAULT(0),"
            + CATEGORY_ID  + " INTEGER NOT NULL, "
            + " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES categories(category_id));";
//            + " (" + KEY_TASK_LIST_ID + ") NOT NULL)";

    //Table Steps
    public static final String TABLE_STEPS = "steps";
    public static final String KEY_STEP_ID = "step_id";
    public static final String KEY_STEP_CONTENT = "content";
    public static final String KEY_STEP_COMPLETE = "complete";
    public static final String TASK_ID = "task_id";
    public static final String TABLE_STEP_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STEPS + " ("
            + KEY_STEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STEP_CONTENT + " TEXT NOT NULL, "
            + KEY_STEP_COMPLETE + "INTEGER DEFAULT (0), "
            + TASK_ID + " INTEGER REFERENCES " + TABLE_TASKS + " (" + KEY_TASK_ID + ") NOT NULL)";


    private static SQLiteDatabase db;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_STEP_STRUCTURE);
        db.execSQL(TABLE_TASKS_STRUCTURE);
        db.execSQL(TABLE_TASK_LIST_STRUCTURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    static SQLiteDatabase getDatabase(Context context) {
        if (db != null) {
            return db;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        return db;
    }
}
