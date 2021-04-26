package com.htc.avkrivonogov.mynotes.models;

import java.util.List;

public class Category {
  private int id;
  private String title;
  private List<Task> taskList;

  public Category(int id, String title, List<Task> taskList) {
    this.id = id;
    this.title = title;
    this.taskList = taskList;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public List<Task> getTaskList() {
    return taskList;
  }
  public void setTaskList(List<Task> taskList) {
    this.taskList = taskList;
  }
}
