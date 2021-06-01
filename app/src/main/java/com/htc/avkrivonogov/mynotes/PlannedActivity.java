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
import com.htc.avkrivonogov.mynotes.controllers.comparators.SortCompleteDate;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.models.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity задач с установленной датой выполнения.
 */
public class PlannedActivity extends AppCompatActivity {

  DatabaseHelper databaseHelper;
  SQLiteDatabase db;
  Cursor cursor;

  List<Task> tasksList = new ArrayList<>();

  RecyclerView recyclerView;
  TaskAdapter taskAdapter;
  TaskAdapter.OnTaskClickListener taskClickListener;

  Toolbar toolbar;

  private boolean isSort;

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
      intent.putExtra("previouslyActivity", "PlannedActivity");
      startActivity(intent);
    };

    cursor = TaskMethods.getCursorFromPlannedTask(db);
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_ID));
      String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_NAME));
      String description =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_DESCRIPTION));
      String image = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_IMAGE));
      String creation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_CREATION));
      String completeDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_DATE));
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
              creation,
              completeDate,
              reminderDate,
              reminderTime,
              listTaskId,
              complete, completeStep);
      if (completeDate != null) {
        tasksList.add(task);
      }
      Collections.sort(tasksList, new SortCompleteDate());
      taskAdapter = new TaskAdapter(this, taskClickListener, tasksList);
      recyclerView.setAdapter(taskAdapter);
    }

    toolbar = findViewById(R.id.planned_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    FloatingActionButton fab = findViewById(R.id.fab_add);
    Intent intent = new Intent(this, EditTaskActivity.class);
    intent.putExtra("previouslyActivity", "PlannedActivity");
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