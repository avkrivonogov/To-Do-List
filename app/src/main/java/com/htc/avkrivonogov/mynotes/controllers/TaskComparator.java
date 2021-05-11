package com.htc.avkrivonogov.mynotes.controllers;

import androidx.annotation.NonNull;

import com.htc.avkrivonogov.mynotes.models.Task;

import java.util.List;

public class TaskComparator {
    private boolean isSortByAsc;

    public void sortTaskByCreation(@NonNull List<Task> tasks) {
        if (isSortByAsc) {
            tasks.sort(((o1, o2) -> o2.getCreation().compareTo(o1.getCreation())));
            isSortByAsc = false;
        } else {
            tasks.sort(((o1, o2) -> o1.getCreation().compareTo(o2.getCreation())));
            isSortByAsc = true;
        }
    }

    public void sortTaskByComplete(@NonNull List<Task> tasks) {
        if (isSortByAsc) {
            tasks.sort(((o1, o2) -> o2.getCompleteDate().compareTo(o1.getCompleteDate())));
            isSortByAsc = false;
        } else {
            tasks.sort(((o1, o2) -> o1.getCompleteDate().compareTo(o2.getCompleteDate())));
            isSortByAsc = true;
        }
    }
}
