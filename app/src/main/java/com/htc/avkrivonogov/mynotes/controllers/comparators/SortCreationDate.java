package com.htc.avkrivonogov.mynotes.controllers.comparators;

import com.htc.avkrivonogov.mynotes.models.Task;
import java.util.Comparator;

/**
 * Сортировка по дате создания.
 */
public class SortCreationDate implements Comparator<Task> {
  @Override
  public int compare(Task o1, Task o2) {
    return o1.getCreationDate().compareTo(o2.getCreationDate());
  }
}
