package com.htc.avkrivonogov.mynotes.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Класс для работы с данными списка задач из базы данных.
 */
public class TasksListMethods {

  public static Cursor getCursorTaskList(SQLiteDatabase db) {
    return db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_TASK_LIST, null);
  }

  /**
   * Создание нового списка задач.
   *
   * @param db База данных.
   * @param title Название списка.
   */
  public static void createTaskList(SQLiteDatabase db, String title) {
    db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_TASK_LIST + " ("
        + DatabaseHelper.KEY_TASK_LIST_NAME + ")" + "VALUES ('" + title + "');");
  }

  /**
   * Получение Cursor, содержащего название списка задач.
   *
   * @param db База данных.
   * @return cursor со списком.
   */
  public static Cursor getCursorTaskListTitle(SQLiteDatabase db) {
    return db.rawQuery("SELECT " + DatabaseHelper.KEY_TASK_LIST_NAME + " FROM "
        + DatabaseHelper.TABLE_TASK_LIST, null);
  }

  /**
   * Получение курсора по id списка.
   *
   * @param db База данных.
   * @param listTaskId списка задач.
   * @return cursor со списком.
   */
  public static Cursor getCursorTaskListFromId(SQLiteDatabase db, int listTaskId) {
    return db.rawQuery("SELECT " + DatabaseHelper.KEY_TASK_LIST_NAME
                + " FROM " + DatabaseHelper.TABLE_TASK_LIST + " WHERE "
                + DatabaseHelper.KEY_TASK_LIST_ID + " = " + listTaskId, null);
  }

  /**
   * Удаление списка задач.
   *
   * @param db База данных.
   * @param taskListId Список задач.
   */
  public static void delete(SQLiteDatabase db, int taskListId) {
    db.execSQL("DELETE FROM " + DatabaseHelper.TABLE_TASK_LIST + " WHERE "
        + DatabaseHelper.KEY_TASK_LIST_ID + " = " + taskListId + ";");
  }

  /**
   * Изменение имени списка задач.
   *
   * @param db База данных.
   * @param taskListId Id списка задач.
   * @param title Заголовок списка задач.
   */
  public static void rename(SQLiteDatabase db, int taskListId, String title) {
    db.execSQL("UPDATE " + DatabaseHelper.TABLE_TASK_LIST + " SET "
        + DatabaseHelper.KEY_TASK_LIST_NAME + " = '" + title
        + "' WHERE " + DatabaseHelper.KEY_TASK_LIST_ID + " = " + taskListId + ";");
  }
}
