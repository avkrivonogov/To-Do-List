package com.htc.avkrivonogov.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.TaskAdapter;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.data.TasksListMethods;
import com.htc.avkrivonogov.mynotes.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksListActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    RecyclerView recyclerView;
    List<Task> taskList = new ArrayList<>();
    int tasksListId;
    TaskAdapter taskAdapter;
    TaskAdapter.OnTaskClickListener taskClickListener;
    FloatingActionButton fab;
    TextView listTitle;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        recyclerView = findViewById(R.id.rv_tasks_list);

        taskClickListener = (task, position) -> {
            Intent intent = new Intent(getApplicationContext(), SingleTaskActivity.class);
            intent.putExtra("id", task.getId());
            startActivity(intent);
        };

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.tasks_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        tasksListId = bundle.getInt("id");
        listTitle = findViewById(R.id.tasks_list_title);
        listTitle.setText(bundle.getString("title"));

        cursor = TaskMethods.getCursorFromTaskList(db, tasksListId);
        while (cursor.moveToNext()) {
            Task task = TaskMethods.getTaskFromCursor(cursor);
            taskList.add(task);
        }
        taskList.sort(((o1, o2) -> o1.getCreation().compareTo(o2.getCreation())));
        taskAdapter = new TaskAdapter(this, taskClickListener, taskList);
        recyclerView.setAdapter(taskAdapter);


        fab = (FloatingActionButton) findViewById(R.id.tasks_list_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaskActivity.start(TasksListActivity.this, null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_list) {
            TasksListMethods.delete(db, tasksListId);
            finish();
        } else if (item.getItemId() == R.id.menu_rename_list) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText renameList = new EditText(this);
            builder.setTitle(R.string.rename_list)
                    .setView(renameList)
                    .setPositiveButton(R.string.rename, (dialog, which) ->
                            action(renameList.getText().toString()));
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    public void action(String title) {
        TasksListMethods.rename(db, tasksListId, title);
        listTitle.setText(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }
}