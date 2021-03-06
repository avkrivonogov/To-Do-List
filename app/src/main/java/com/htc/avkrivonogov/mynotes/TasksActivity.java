package com.htc.avkrivonogov.mynotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.TaskAdapter;
import com.htc.avkrivonogov.mynotes.controllers.comparators.SortCreationDate;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.models.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity задач без установленной даты выполнения.
 */
public class TasksActivity extends AppCompatActivity {

  DatabaseHelper databaseHelper;
  SQLiteDatabase db;
  Cursor cursor;

  List<Task> tasksList = new ArrayList<>();
  TaskAdapter taskAdapter;
  TaskAdapter.OnTaskClickListener taskClickListener;
  RecyclerView recyclerView;
  Toolbar toolbar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tasks);

    databaseHelper = new DatabaseHelper(getApplicationContext());
    db = databaseHelper.getReadableDatabase();

    recyclerView = findViewById(R.id.rv_tasks);
    taskClickListener = (task, position) -> {
      Intent intent = new Intent(getApplicationContext(), SingleTaskActivity.class);
      intent.putExtra("id", task.getId());
      intent.putExtra("previouslyActivity", "TasksActivity");
      startActivity(intent);
    };

    cursor = TaskMethods.getCursorFromTaskList(db, 0);
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_ID));
      String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_NAME));
      String description =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_DESCRIPTION));
      String image = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_IMAGE));
      String completeDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_DATE));
      String creationDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_CREATION));
      String reminderDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_REMINDER_DATE));
      String reminderTime =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_REMINDER_TIME));
      int listTaskId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CATEGORY_ID));
      int complete = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_STATUS));
      int completeStep =
              cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_STEPS));
      Task task = new Task(id,
              title,
              description,
              image,
              creationDate,
              completeDate,
              reminderDate,
              reminderTime,
              listTaskId,
              complete, completeStep);
      if (completeDate == null) {
        tasksList.add(task);
      }
    }
    Collections.sort(tasksList, new SortCreationDate());
    taskAdapter = new TaskAdapter(this, taskClickListener, tasksList);
    recyclerView.setAdapter(taskAdapter);

    toolbar = findViewById(R.id.tasks_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
    Intent intent = new Intent(this, EditTaskActivity.class);
    intent.putExtra("previouslyActivity", "TasksActivity");
    fab.setOnClickListener(v -> startActivity(intent));
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
    } else if (item.getItemId() == R.id.menu_sort) {
      Collections.reverse(tasksList);
      taskAdapter = new TaskAdapter(this, taskClickListener, tasksList);
      recyclerView.setAdapter(taskAdapter);
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