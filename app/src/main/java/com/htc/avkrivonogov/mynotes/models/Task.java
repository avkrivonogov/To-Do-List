package com.htc.avkrivonogov.mynotes.models;

import java.util.List;

public class Task {
    private int id;
    private String title;
    private long creation;
    private long reminder;
    private Category category;

    private List<TaskStep> taskSteps;

    public Task(int id, String title, String comment, long creation,
                long reminder, Category category, List<TaskStep> taskSteps) {
        this.id = id;
        this.title = title;
        this.creation = creation;
        this.reminder = reminder;
        this.category = category;
        this.taskSteps = taskSteps;
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

    public List<TaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(List<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }


    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public long getReminder() {
        return reminder;
    }

    public void setReminder() {
        this.reminder = reminder;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
