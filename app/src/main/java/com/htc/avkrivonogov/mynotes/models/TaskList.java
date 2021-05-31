package com.htc.avkrivonogov.mynotes.models;

import java.util.Comparator;
import java.util.List;

/**
 * Модель списка задач.
 */
public class TaskList implements Comparable<TaskList> {

  private int id;
  private String title;

  /**
   * Конструктор списка задач.
   *
   * @param id списка.
   * @param title Заголовок списка.
   */
  public TaskList(int id, String title) {
    this.id = id;
    this.title = title;
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

  @Override
  public int compareTo(TaskList o) {
    return title.compareTo(o.getTitle());
  }
}
