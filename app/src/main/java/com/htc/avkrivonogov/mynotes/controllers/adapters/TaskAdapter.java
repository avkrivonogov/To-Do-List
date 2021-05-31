package com.htc.avkrivonogov.mynotes.controllers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htc.avkrivonogov.mynotes.R;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.data.TaskStepsMethods;
import com.htc.avkrivonogov.mynotes.models.Task;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Реализация адаптера для задач.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

  /**
   * Интерфейс для обработки нажатия на элемент RecyclerView.
   */
  public interface OnTaskClickListener {
    void onTaskClick(Task task, int position);
  }

  private OnTaskClickListener onTaskClickListener;
  private List<Task> tasks;
  private LayoutInflater inflater;

  DatabaseHelper databaseHelper;
  SQLiteDatabase db;

  Context context;

  /**
   * Конструктор адаптера.
   *
   * @param context Контекст.
   * @param onTaskClick Описание поведения при нажатии на элемент RecyclerView.
   * @param tasks Задачи.
   */
  public TaskAdapter(Context context, OnTaskClickListener onTaskClick,
                       List<Task> tasks) {
    this.context = context;
    this.onTaskClickListener = onTaskClick;
    this.inflater = LayoutInflater.from(context);
    this.tasks = tasks;

    databaseHelper  = new DatabaseHelper(context);
    db = databaseHelper.getReadableDatabase();
  }

  @NonNull
  @Override
  public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.tasks_list, parent, false);
    return new TaskAdapter.ViewHolder(itemView);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Task task = tasks.get(position);
    holder.title.setText(task.getTitle());
    if (task.getImage() != null) {
      Uri uri = Uri.parse(task.getImage());
      try {
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        final Bitmap selectedImages = BitmapFactory.decodeStream(imageStream);
        holder.image.setImageBitmap(selectedImages);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    holder.completeTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        task.setCompleteStatus(1);
        holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      } else {
        task.setCompleteStatus(0);
        holder.title.setPaintFlags(holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
      }
      TaskMethods.statusCompleteTask(db, task);
    });

    int allSteps = TaskStepsMethods.getAllSteps(db, task.getId());
    String of = context.getString(R.string.of);
    String stepComplete = context.getString(R.string.step_complete);
    String noSteps = context.getString(R.string.no_steps);

    if (allSteps != 0) {
      holder.countSteps.setText(TaskStepsMethods.getCompleteStepsTask(db, task.getId())
              + " " + of + " " + TaskStepsMethods.getAllSteps(db, task.getId())
              + " " + stepComplete);
    } else {
      holder.countSteps.setText(noSteps);
    }
    String completeDateNoSelected = context.getString(R.string.no_choose_complete_date);
    holder.dateComplete.setText(task.getCompleteDate() != null
            ? task.getCompleteDate() : completeDateNoSelected);

    holder.itemView.setOnClickListener(v -> onTaskClickListener.onTaskClick(task, position));
    if (task.getCompleteStatus() == 1) {
      holder.completeTask.setChecked(true);
    }
  }

  @Override
  public int getItemCount() {
    return tasks.size();
  }

  /**
   * Описание строки RecyclerView.
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {
    CheckBox completeTask;
    TextView title;
    TextView countSteps;
    TextView dateComplete;
    ImageView image;

    /**
     * Конструктор описания строки.
     *
     * @param itemView для описания строки RecyclerView.
     */
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      completeTask = itemView.findViewById(R.id.complete_task);
      title = itemView.findViewById(R.id.task_title_in_list);
      countSteps = itemView.findViewById(R.id.count_steps);
      dateComplete = itemView.findViewById(R.id.date_complete);
      image = itemView.findViewById(R.id.image_task_in_list);
    }
  }
}
