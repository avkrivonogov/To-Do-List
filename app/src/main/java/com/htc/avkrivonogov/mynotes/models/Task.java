package com.htc.avkrivonogov.mynotes.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Модель задачи.
 */
public class Task {

  private int id;
  private String title;
  private String description;
  private String image;
  private String creationDate;
  private String completeDate;
  private String reminderDate;
  private String reminderTime;
  private int taskListId;
  private int completeStatus;
  private int completeStep;

  /**
   * Конструктор задачи.
   *
   * @param id задачи.
   * @param title Заголовок задачи.
   * @param description Поясняющий текст.
   * @param image Картинка.
   * @param creationDate Дата создания.
   * @param completeDate  Дата выполнения.
   * @param reminderDate Дата напоминания.
   * @param reminderTime Время напоминания.
   * @param taskListId id списка задача, к которому относится задача.
   * @param completeStatus Статус задачи.
   * @param completeStep Выполненные шаги задачи.
   */
  public Task(int id, String title, String description, String image, String creationDate,
                String completeDate, String reminderDate, String reminderTime,
                int taskListId, int completeStatus, int completeStep) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.image = image;
    this.creationDate = creationDate;
    this.completeDate = completeDate;
    this.reminderDate = reminderDate;
    this.reminderTime = reminderTime;
    this.taskListId = taskListId;
    this.completeStatus = completeStatus;
    this.completeStep = completeStep;
  }

  /**
   * Конструктор задачи без идентификатора.
   *
   * @param title Заголовок задачи.
   * @param description Поясняющий текст.
   * @param image Картинка.
   * @param creationDate Дата создания.
   * @param completeDate  Дата выполнения.
   * @param reminderDate Дата напоминания.
   * @param reminderTime Время напоминания.
   * @param taskListId id списка задача, к которому относится задача.
   * @param completeStatus Статус задачи.
   * @param completeStep Выполненные шаги задачи.
   */
  public Task(String title, String description, String image, String creationDate,
                String completeDate, String reminderDate, String reminderTime,
                int taskListId, int completeStatus, int completeStep) {
    this.title = title;
    this.description = description;
    this.image = image;
    this.creationDate = creationDate;
    this.completeDate = completeDate;
    this.reminderDate = reminderDate;
    this.reminderTime = reminderTime;
    this.taskListId = taskListId;
    this.completeStatus = completeStatus;
    this.completeStep = completeStep;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(String completeDate) {
    this.completeDate = completeDate;
  }

  public String getReminderDate() {
    return reminderDate;
  }

  public void setReminderDate(String reminderDate) {
    this.reminderDate = reminderDate;
  }

  public String getReminderTime() {
    return reminderTime;
  }

  public void setReminderTime(String reminderTime) {
    this.reminderTime = reminderTime;
  }

  public int getTaskListId() {
    return taskListId;
  }

  public void setTaskListId(int taskListId) {
    this.taskListId = taskListId;
  }

  public int getCompleteStatus() {
    return completeStatus;
  }

  public void setCompleteStatus(int completeStatus) {
    this.completeStatus = completeStatus;
  }

  public int getCompleteStep() {
    return completeStep;
  }

  public void setCompleteStep(int completeStep) {
    this.completeStep = completeStep;
  }

  public Date getDateFromString() {
    Date date = null;
    DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    String strDate = getCompleteDate();
    try {
      date = format.parse(strDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

//  @Override
//  public int compareTo(Task o) {
//    return creationDate.compareTo(o.getCreationDate());
//  }
}
