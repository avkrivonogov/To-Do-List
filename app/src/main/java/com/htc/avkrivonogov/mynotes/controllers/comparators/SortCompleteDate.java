package com.htc.avkrivonogov.mynotes.controllers.comparators;

import com.htc.avkrivonogov.mynotes.models.Task;
import java.util.Comparator;

/**
 * Сортировка по дате выполнения.
 */
public class SortCompleteDate implements Comparator<Task> {
  private boolean isSort;

  @Override
  public int compare(Task o1, Task o2) {
    return o1.getCompleteDate().compareTo(o2.getCompleteDate());
  }
}
