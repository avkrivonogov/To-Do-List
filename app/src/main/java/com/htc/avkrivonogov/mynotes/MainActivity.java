package com.htc.avkrivonogov.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.TasksListAdapter;
import com.htc.avkrivonogov.mynotes.controllers.dialogs.CreateNewTasksList;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TasksList;
import com.htc.avkrivonogov.mynotes.models.TaskList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    Button buttonTasks;
    Button buttonPlanned;
    RecyclerView recyclerView;
    List<TaskList> tasksList = new ArrayList<>();
    TasksListAdapter adapter;
    TasksListAdapter.OnTaskListClickListener listClickListener;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

//        Collections.sort(tasksList);

        listClickListener = (taskList, position) -> {
            Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
            intent.putExtra("id", taskList.getId());
            intent.putExtra("title", taskList.getTitle());
            startActivity(intent);
        };
        recyclerView = findViewById(R.id.rv_main);
        adapter = new TasksListAdapter(this, listClickListener, tasksList);
        recyclerView.setAdapter(adapter);
        cursor = TasksList.getCursorTaskList(db);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_ID));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_NAME));
            TaskList taskList = new TaskList(id, title);
            tasksList.add(taskList);
        }

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        buttonTasks = (Button) findViewById(R.id.button_tasks);
        buttonPlanned = (Button) findViewById(R.id.button_planned);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> EditTaskActivity.start(MainActivity.this, null));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort:
                finish();
            case R.id.menu_add_new_list:
                CreateNewTasksList createDialog = new CreateNewTasksList();
                createDialog.show(getSupportFragmentManager(), "createNeList");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText newList = new EditText(this);
                builder.setTitle(R.string.create_new_list)
                        .setView(newList)
                        .setPositiveButton(R.string.create, (dialog, which) ->
                                action(newList.getText().toString()));
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void action(String title) {
        TasksList.createTaskList(db, title);
        cursor = TasksList.getCursorTaskList(db);
        tasksList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_ID));
            title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_NAME));
            TaskList taskList = new TaskList(id, title);
            tasksList.add(taskList);
        }
        Collections.sort(tasksList, (o1, o2) -> o1.toString().compareTo(o2.toString()));
        adapter = new TasksListAdapter(this, listClickListener, tasksList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }

    public void goToTasks(View view) {
        Intent intent = new Intent(this, Tasks.class);
        startActivity(intent);
    }

    public void goToPlanned(View view) {
        Intent intent = new Intent(this, PlannedActivity.class);
        startActivity(intent);
    }
}