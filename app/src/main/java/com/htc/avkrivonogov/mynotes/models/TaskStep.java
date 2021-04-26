package com.htc.avkrivonogov.mynotes.models;

public class TaskStep {

    private int id;
    private String step;
    private boolean done;

    public TaskStep(int id, String step, boolean done) {
        this.id = id;
        this.step = step;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
