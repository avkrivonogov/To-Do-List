package com.htc.avkrivonogov.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

/**
 * Класс для работы с базой данных.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

  public static final String DB_NAME = "notes.db";
  public static final int DB_VERSION = 1;

  //Table TaskList
  public static final String TABLE_TASK_LIST = "categories";
  public static final String KEY_TASK_LIST_ID = "category_id";
  public static final String KEY_TASK_LIST_NAME = "name";

  //Table Tasks
  public static final String TABLE_TASKS = "tasks";
  public static final String KEY_TASK_ID = "task_id";
  public static final String KEY_TASK_NAME = "name";
  public static final String KEY_TASK_DESCRIPTION = "description";
  public static final String KEY_TASK_IMAGE = "image";
  public static final String KEY_TASK_CREATION = "creation";
  public static final String KEY_TASK_COMPLETE_DATE = "complete_date";
  public static final String KEY_TASK_REMINDER_DATE = "reminder_date";
  public static final String KEY_TASK_REMINDER_TIME = "reminder_time";
  public static final String KEY_TASK_STATUS = "complete_status";
  public static final String KEY_TASK_COMPLETE_STEPS = "complete_steps";
  public static final String CATEGORY_ID = "category_id";

  //Table Steps
  public static final String TABLE_STEPS = "steps";
  public static final String KEY_STEP_ID = "step_id";
  public static final String KEY_STEP_CONTENT = "content";
  public static final String KEY_STEP_COMPLETE = "complete";
  public static final String TASK_ID = "task_id";

  private static SQLiteDatabase db;

  public DatabaseHelper(@Nullable Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE "
            + TABLE_TASK_LIST + " ("
            + KEY_TASK_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASK_LIST_NAME + " TEXT NOT NULL);");
    db.execSQL("CREATE TABLE "
            + TABLE_TASKS + " ("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASK_NAME + " TEXT NOT NULL, "
            + KEY_TASK_DESCRIPTION + " TEXT, "
            + KEY_TASK_IMAGE + " TEXT, "
            + KEY_TASK_CREATION + " TEXT, "
            + KEY_TASK_COMPLETE_DATE + " TEXT, "
            + KEY_TASK_REMINDER_DATE + " TEXT, "
            + KEY_TASK_REMINDER_TIME + " TEXT, "
            + KEY_TASK_STATUS + " INTEGER DEFAULT(0),"
            + KEY_TASK_COMPLETE_STEPS + " INTEGER DEFAULT(0), "
            + CATEGORY_ID  + " INTEGER NOT NULL, "
            + " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES categories(category_id));");
    db.execSQL("CREATE TABLE "
            + TABLE_STEPS + " ("
            + KEY_STEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STEP_CONTENT + " TEXT NOT NULL, "
            + KEY_STEP_COMPLETE + " INTEGER DEFAULT (0), "
            + TASK_ID + " INTEGER REFERENCES "
            + TABLE_TASKS + " (" + KEY_TASK_ID + ") NOT NULL);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}
