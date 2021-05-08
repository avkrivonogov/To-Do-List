package com.htc.avkrivonogov.mynotes.controllers.adapters;

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
import com.htc.avkrivonogov.mynotes.models.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    public interface OnTaskClickListener {
        void onTaskClick(Task task, int position);
    }

    private TaskAdapter.OnTaskClickListener onTaskClickListener;
    private List<Task> tasks;
    private LayoutInflater inflater;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list, parent, false);
        return new TaskAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.completeTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                task.setCompleteStatus(1);
            } else {
                task.setCompleteStatus(0);
            }
        });
        holder.itemView.setOnClickListener(v -> onTaskClickListener.onTaskClick(task, position));
        if (task.getCompleteStatus() == 1) {
            holder.completeTask.setChecked(true);
            holder.title.setPaintFlags(holder.title.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() &
                    (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.title.setText(task.getTitle());
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox completeTask;
        TextView title;
        TextView countSteps;
        TextView dateComplete;
        ImageView image;

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
