package com.htc.avkrivonogov.mynotes.models;

import android.graphics.Bitmap;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private String description;
    private Bitmap image;
    private LocalDateTime creation;
    private LocalDate completeDate;
    private LocalDateTime reminder;
    private int taskListId;
    private int completeStatus;

    public Task(int id, String title, String description, Bitmap image, LocalDateTime creation,
                LocalDate completeDate, LocalDateTime reminder,
                int taskListId, int completeStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.creation = creation;
        this.completeDate = completeDate;
        this.reminder = reminder;
        this.taskListId = taskListId;
        this.completeStatus = completeStatus;
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

    public LocalDate getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDate completeDate) {
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

    public int getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(int completeStatus) {
        this.completeStatus = completeStatus;
    }
}
