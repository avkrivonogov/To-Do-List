package com.htc.avkrivonogov.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.htc.avkrivonogov.mynotes.controllers.adapters.TaskAdapter;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TasksList;
import com.htc.avkrivonogov.mynotes.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksListActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    RecyclerView recyclerView;
    List<Task> taskList = new ArrayList<>();
    int tasksListId;
    TaskAdapter taskAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rv_tasks_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.menu_delete_list:
                TasksList.delete(db, tasksListId);
                finish();
            case R.id.menu_sort_list:
                finish();
            case R.id.menu_rename_list:
                finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}