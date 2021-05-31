package com.htc.avkrivonogov.mynotes.data;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.htc.avkrivonogov.mynotes.models.Task;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

/**
 * Класс для работы с данными задачи из базы данных.
 */
public class TaskMethods {

  /**
   * Выполнение запроса дл получения Cursor
   * с задачей, найденной по id.
   *
   * @param db База данных.
   * @param id Задачи.
   * @return cursor с задачей.
   */
  public static Cursor getCursorFromId(SQLiteDatabase db, int id) {
    return db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_TASKS
        + " WHERE " + DatabaseHelper.KEY_TASK_ID + " = " + id, null);
  }

  /**
   * Получения Cursor с задачей, найденной по id списка.
   *
   * @param db База данных.
   * @param listId Списка.
   * @return cursor с задачей.
   */
  public static Cursor getCursorFromTaskList(SQLiteDatabase db, int listId) {
    return db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_TASKS
        + " WHERE " + DatabaseHelper.CATEGORY_ID + " = " + listId, null);
  }

  /**
   * Получение Cursor, содержащем задачу с датой выполения.
   *
   * @param db База данных.
   * @return Cursor.
   */
  public static Cursor getCursorFromPlannedTask(SQLiteDatabase db) {
    return db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_TASKS
        + " WHERE " + DatabaseHelper.KEY_TASK_COMPLETE_DATE + " IS NOT NULL", null);
  }

  /**
   * Обновление задачи.
   *
   * @param db База данных.
   * @param task Задача.
   */
  public static void updateTask(SQLiteDatabase db, Task task) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(DatabaseHelper.KEY_TASK_NAME, task.getTitle());
    contentValues.put(DatabaseHelper.KEY_TASK_CREATION, task.getCreationDate());
    contentValues.put(DatabaseHelper.CATEGORY_ID, task.getTaskListId());
    if (task.getDescription() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_DESCRIPTION, task.getDescription());
    }
    if (task.getImage() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_IMAGE, task.getImage());
    }
    if (task.getCompleteDate() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_COMPLETE_DATE, task.getCompleteDate());
    }
    if (task.getReminderDate() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_REMINDER_DATE, task.getReminderDate());
    }
    if (task.getReminderTime() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_REMINDER_TIME, task.getReminderTime());
    }
    db.update(DatabaseHelper.TABLE_TASKS, contentValues,
            DatabaseHelper.KEY_TASK_ID + "=" + task.getId(), null);
  }

  /**
   * Добавление задачи.
   *
   * @param db База данных.
   * @param task Задача.
   */
  public static void addTask(SQLiteDatabase db, Task task) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(DatabaseHelper.KEY_TASK_NAME, task.getTitle());
    contentValues.put(DatabaseHelper.KEY_TASK_CREATION, task.getCreationDate());
    contentValues.put(DatabaseHelper.CATEGORY_ID, task.getTaskListId());
    if (task.getDescription() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_DESCRIPTION, task.getDescription());
    }
    if (task.getImage() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_IMAGE, task.getImage());
    }
    if (task.getCompleteDate() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_COMPLETE_DATE, task.getCompleteDate());
    }
    if (task.getReminderDate() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_REMINDER_DATE, task.getReminderDate());
    }
    if (task.getReminderTime() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_REMINDER_TIME, task.getReminderTime());
    }
    db.insert(DatabaseHelper.TABLE_TASKS, null, contentValues);
  }

  /**
   * id последней задачи.
   *
   * @param db База данных.
   * @return id последней задачи.
   */
  public static int taskLastId(SQLiteDatabase db) {
    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TASKS,
                null);
    cursor.moveToFirst();
    int id = cursor.getInt(0);
    cursor.close();
    return id;
  }

  /**
   * Удаление задачи.
   *
   * @param db База данных.
   * @param id задачи.
   */
  public static void delete(SQLiteDatabase db, int id) {
    db.execSQL("DELETE FROM " + DatabaseHelper.TABLE_TASKS + " WHERE "
            + DatabaseHelper.KEY_TASK_ID + " = " + id + ";");
  }

  /**
   * Установка статуса выполнения задачи.
   *
   * @param db База данных.
   * @param task Задача.
   */
  public static void statusCompleteTask(SQLiteDatabase db, Task task) {
    db.execSQL("UPDATE " + DatabaseHelper.TABLE_TASKS + " SET "
        + DatabaseHelper.KEY_TASK_STATUS + " = '" + task.getCompleteStatus()
        + "' WHERE " + DatabaseHelper.KEY_TASK_ID + " = " + task.getId() + ";");
  }

  /**
   * Увеличение завершенных шагов задачи.
   *
   * @param db База данных.
   * @param step Шаг задачи.
   * @param completeStep Завершенный шаг.
   */
  public static void plusCompleteSteps(SQLiteDatabase db, TaskStep step, int completeStep) {
    db.execSQL("UPDATE " + DatabaseHelper.TABLE_TASKS
        + " SET " + DatabaseHelper.KEY_TASK_COMPLETE_STEPS + " = " + (completeStep + 1)
        + " WHERE " + DatabaseHelper.KEY_TASK_ID + " = " + step.getTaskId());

  }

  /**
   * Уменьшение завершенных шагов задачи.
   *
   * @param db База данных.
   * @param step Шаг задачи.
   * @param completeStep Завершенный шаг.
   */
  public static void minusCompleteSteps(SQLiteDatabase db, TaskStep step, int completeStep) {
    db.execSQL("UPDATE " + DatabaseHelper.TABLE_TASKS
                + " SET " + DatabaseHelper.KEY_TASK_COMPLETE_STEPS + " = " + (completeStep - 1)
                + " WHERE " + DatabaseHelper.KEY_TASK_ID + " = " + step.getTaskId());

  }

  private static ContentValues contentValuesForTask(Task task) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(DatabaseHelper.KEY_TASK_NAME, task.getTitle());
    contentValues.put(DatabaseHelper.KEY_TASK_CREATION, task.getCreationDate());
    contentValues.put(DatabaseHelper.CATEGORY_ID, task.getTaskListId());
    if (task.getDescription() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_DESCRIPTION, task.getDescription());
    }
    if (task.getImage() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_IMAGE, task.getImage());
    }
    if (task.getCompleteDate() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_COMPLETE_DATE, task.getCompleteDate());
    }
    if (task.getReminderDate() != null) {
      contentValues.put(DatabaseHelper.KEY_TASK_REMINDER_DATE, task.getReminderDate());
    }
    return contentValues;
  }
}
