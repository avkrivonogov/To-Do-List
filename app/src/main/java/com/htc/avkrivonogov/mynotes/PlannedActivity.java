package com.htc.avkrivonogov.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.TaskAdapter;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.models.Task;

import java.util.ArrayList;
import java.util.List;

public class PlannedActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    List<Task> taskList = new ArrayList<>();

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    TaskAdapter.OnTaskClickListener taskClickListener;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.rv_planned);
        taskClickListener = (task, position) -> {
            Intent intent = new Intent(getApplicationContext(), SingleTaskActivity.class);
            intent.putExtra("id", task.getId());
            startActivity(intent);
        };

        cursor = TaskMethods.getCursorFromPlannedTask(db);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.planned_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> EditTaskActivity.start(PlannedActivity.this, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            //использовать switch
//        } else if (item.getItemId() == R.id.menu_sort) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }
}