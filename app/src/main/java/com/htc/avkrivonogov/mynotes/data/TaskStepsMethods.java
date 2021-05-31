package com.htc.avkrivonogov.mynotes.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.htc.avkrivonogov.mynotes.models.TaskStep;
import java.util.List;

/**
 * Методы для работы с шагами задачи из базы данных.
 */
public class TaskStepsMethods {
  /**
   * Выполнение запроса к базе данных.
   *
   * @param db База данных.
   * @param id Задачи.
   * @param stepList Список шагов.
   */
  public static void insert(SQLiteDatabase db, int id, List<String> stepList) {
    for (String item : stepList) {
      db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_STEPS + " ('"
            + DatabaseHelper.TASK_ID + "', '" + DatabaseHelper.KEY_STEP_CONTENT
            + "', '" + DatabaseHelper.KEY_STEP_COMPLETE + "')" + "VALUES ('"
            + id + "', '" + item + "', " + 0 + ")");
    }
  }

  /**
   * Получение cursor с шагами, соотвествующих задаче.
   *
   * @param db База данных.
   * @param id задачи.
   * @return cursor.
   */
  public static Cursor getCursorFromTaskId(SQLiteDatabase db, int id) {
    return db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_STEPS
            + " WHERE " + DatabaseHelper.TASK_ID + " = " + id, null);
  }

  /**
   * Получение cursor с шагами, соответсвующих задаче.
   *
   * @param db База данных.
   * @param title название задачи.
   * @return cursor.
   */
  public static Cursor getCursorFromTaskTitle(SQLiteDatabase db, String title) {
    return db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_STEPS
                + " WHERE " + DatabaseHelper.TASK_ID + " = "
                + "(SELECT id FROM " + DatabaseHelper.TABLE_TASKS
                + " WHERE " + DatabaseHelper.KEY_TASK_NAME + " = '" + title + "')", null);
  }

  /**
   * Выполнение запроса для обновления статуса задачи.
   *
   * @param db База данных.
   * @param step Шаг задачи.
   */
  public static void updateStep(SQLiteDatabase db, TaskStep step) {
    db.execSQL("UPDATE " + DatabaseHelper.TABLE_STEPS + " SET "
        + DatabaseHelper.KEY_STEP_COMPLETE + " = " + step.getComplete()
        + " WHERE " + DatabaseHelper.KEY_STEP_ID + " = " + step.getId() + ";");
  }

  /**
   * Удаление шага задачи из базы данных.
   *
   * @param db База данных.
   * @param id Шага.
   */
  public static void deleteStep(SQLiteDatabase db, int id) {
    db.delete(DatabaseHelper.TABLE_STEPS, DatabaseHelper.KEY_STEP_ID + " = "
            + id, null);
  }

  /**
   * Удвление шага задачи из базы данных по идентификатору задачи.
   *
   * @param db База данных.
   * @param taskId Идентификатор задачи.
   */
  public static void deleteStepByTaskId(SQLiteDatabase db, int taskId) {
    db.delete(DatabaseHelper.TABLE_STEPS, DatabaseHelper.TASK_ID + " = "
            + taskId, null);
  }

  /**
     * Количество шагов задачи.
     *
     * @param db База данных.
     * @param taskId задачи.
     * @return Количество шагов задачи.
     */
  public static int getAllSteps(SQLiteDatabase db, int taskId) {
    int allSteps = 0;
    Cursor cursor = null;
    try {
      cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_STEPS
                    + " WHERE " + DatabaseHelper.TASK_ID + " = " + taskId, null);
      cursor.moveToFirst();
      allSteps = cursor.getInt(0);
      cursor.close();
    } catch (Exception e) {
      cursor.close();
    }
    return allSteps;
  }

  /**
   * Количество выполненных шагов задачи.
   *
   * @param db База данных.
   * @param taskId Задачи.
   * @return Количество выполненных шагов задачи.
   */
  public static int getCompleteStepsTask(SQLiteDatabase db, int taskId) {
    Cursor cursor = db.rawQuery("SELECT COUNT(" + DatabaseHelper.KEY_STEP_COMPLETE + ") "
                + "FROM " + DatabaseHelper.TABLE_STEPS
                + " WHERE " + DatabaseHelper.KEY_STEP_COMPLETE
                + " = 1 AND " + DatabaseHelper.TASK_ID + " = " + taskId, null);
    cursor.moveToFirst();
    int completeSteps = cursor.getInt(0);
    cursor.close();
    return completeSteps;
  }

  /**
   * Получение cursor с выполненными шагами задачи.
   *
   * @param db База данных
   * @param stage Шаг задачию
   * @return cursor.
   */
  public static Cursor getCursorFromCompleteStep(SQLiteDatabase db, TaskStep stage) {
    return db.rawQuery("SELECT " + DatabaseHelper.KEY_TASK_COMPLETE_STEPS
                + " FROM " + DatabaseHelper.TABLE_TASKS
                + " WHERE " + DatabaseHelper.KEY_TASK_ID + " = " + stage.getTaskId(), null);
  }
}
