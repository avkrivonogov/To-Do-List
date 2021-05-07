package com.htc.avkrivonogov.mynotes.models;

import android.graphics.Bitmap;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private String description;
    private Bitmap image;
    private LocalDateTime creation;
    private LocalDateTime completeDate;
    private LocalDateTime reminder;
    private int taskListId;
    private int complete;

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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public LocalDateTime getReminder() {
        return reminder;
    }

    public void setReminder(LocalDateTime reminder) {
        this.reminder = reminder;
    }

    public int getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(int taskListId) {
        this.taskListId = taskListId;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }
}
