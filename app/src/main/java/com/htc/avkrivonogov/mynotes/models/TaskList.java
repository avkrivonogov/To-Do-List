package com.htc.avkrivonogov.mynotes.models;

public class TaskList {
  private int id;
  private String title;

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

// ПОдумать надо ли
//  @Override
//  public int compareTo(TaskList o) {
//    return title.compareTo(o.getTitle());
//  }
}
