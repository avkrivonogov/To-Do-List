package com.htc.avkrivonogov.mynotes.models;

public class TaskStep {

    private int id;
    private int taskId;
    private String content;
    private int complete;

    public TaskStep(int id, int taskId, String content, int complete) {
        this.id = id;
        this.taskId = taskId;
        this.content = content;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    //тут еще подумать почему надо сэттер
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }
}
