package com.htc.avkrivonogov.mynotes.controllers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
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
import com.htc.avkrivonogov.mynotes.models.TaskStep;
import java.util.List;

/**
 * Реализация адаптера для шагов.
 */
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
  private LayoutInflater inflater;
  private List<TaskStep> steps;

  DatabaseHelper databaseHelper;
  SQLiteDatabase db;
  Cursor cursor;

  /**
   * Конструктор адаптера.
   *
   * @param context Контекст.
   * @param steps Шаги.
   */
  public StepAdapter(Context context, List<TaskStep> steps) {
    this.inflater = LayoutInflater.from(context);
    this.steps = steps;

    databaseHelper = new DatabaseHelper(context);
    db = databaseHelper.getReadableDatabase();
  }

  @NonNull
  @Override
  public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.task_step_item, parent, false);
    return new StepAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int position) {
    TaskStep step = steps.get(position);
    holder.content.setText(step.getContent());
    holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (step.getComplete() == 1) {
        if (!isChecked) {
          step.setComplete(0);
          holder.content.setPaintFlags(holder.content.getPaintFlags()
                  & ~Paint.STRIKE_THRU_TEXT_FLAG);
          TaskStepsMethods.updateStep(db, step);
          cursor = TaskStepsMethods.getCursorFromCompleteStep(db, step);
          cursor.moveToFirst();
          int completeSteps = cursor.getInt(
                  cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_STEPS));
          TaskMethods.minusCompleteSteps(db, step, completeSteps);
        }
      } else {
        if (isChecked) {
          step.setComplete(1);
          holder.content.setPaintFlags(holder.content.getPaintFlags()
                  | Paint.STRIKE_THRU_TEXT_FLAG);
          TaskStepsMethods.updateStep(db, step);
          cursor = TaskStepsMethods.getCursorFromCompleteStep(db, step);
          cursor.moveToFirst();
          int completeSteps = cursor.getInt(
                  cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_STEPS));
          TaskMethods.plusCompleteSteps(db, step, completeSteps);
        }
      }
    });
    if (step.getComplete() == 1) {
      holder.check.setChecked(true);
    }
    holder.delete.setOnClickListener(v -> {
      steps.remove(position);
      TaskStepsMethods.deleteStep(db, step.getId());
      notifyDataSetChanged();
    });
  }

  @Override
  public int getItemCount() {
    return steps.size();
  }

  /**
   * Описание строки RecyclerView.
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView content;
    CheckBox check;
    ImageView delete;

    /**
     * Конструктор описания строки RecyclerView.
     *
     * @param itemView view для описания строки RecyclerView.
     */
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      content = itemView.findViewById(R.id.step_content);
      check = itemView.findViewById(R.id.complete_step);
      delete = itemView.findViewById(R.id.delete_step);
    }
  }
}