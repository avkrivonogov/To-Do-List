package com.htc.avkrivonogov.mynotes.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htc.avkrivonogov.mynotes.R;
import com.htc.avkrivonogov.mynotes.data.DatabaseHandler;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Activity mActivity;
    private List<TaskStep> steps;
    private DatabaseHandler db;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_step, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    //public delete

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
