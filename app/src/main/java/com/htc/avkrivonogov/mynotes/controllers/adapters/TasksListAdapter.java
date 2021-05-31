package com.htc.avkrivonogov.mynotes.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htc.avkrivonogov.mynotes.R;
import com.htc.avkrivonogov.mynotes.models.TaskList;
import java.util.List;

/**
 * Реализация адаптера для списка задач.
 */
public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> {

  /**
   * Интерфейс для обработки нажатия на элемент RecyclerView.
   */
  public interface OnTaskListClickListener {
    void onTaskListClick(TaskList tasksList, int position);
  }

  private final LayoutInflater inflater;
  private final OnTaskListClickListener onClickListener;
  private final List<TaskList> tasksLists;
  private boolean isSortList;

  /**
   * Конструктор адаптера.
   *
   * @param context Контекст.
   * @param onClickListener Описание поведения при нажатии на элемент RecyclerView.
   * @param tasksLists Списки задач.
   */
  public TasksListAdapter(Context context, OnTaskListClickListener onClickListener,
                            List<TaskList> tasksLists) {
    this.inflater = LayoutInflater.from(context);
    this.onClickListener = onClickListener;
    this.tasksLists = tasksLists;
//    sortTaskListByName();
  }

  @NonNull
  @Override
  public TasksListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.tasks_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    TaskList taskList = tasksLists.get(position);
    holder.listTitle.setText(taskList.getTitle());
    holder.itemView.setOnClickListener(v ->
            onClickListener.onTaskListClick(taskList, position));
  }

  @Override
  public int getItemCount() {
    return tasksLists.size();
  }

  /**
   * Описание строки RecyclerView.
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView listTitle;

    /**
     * Конструктор описания строки RecyclerView.
     *
     * @param itemView view для описания строки RecyclerView.
     */
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      listTitle = itemView.findViewById(R.id.listTitle);
    }
  }

//  public void sortTaskListByName() {
//    if (isSortList) {
//      tasksLists.sort((o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
//      isSortList = false;
//    } else {
//      tasksLists.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
//      isSortList = true;
//    }
//  }
}
