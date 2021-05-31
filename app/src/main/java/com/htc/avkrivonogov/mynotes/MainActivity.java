package com.htc.avkrivonogov.mynotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.TasksListAdapter;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TasksListMethods;
import com.htc.avkrivonogov.mynotes.models.TaskList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Главная Activity приложения.
 */
public class MainActivity extends AppCompatActivity {

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

    recyclerView = findViewById(R.id.rv_main);
    listClickListener = (taskList, position) -> {
      Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
      Collections.sort(tasksList);
      intent.putExtra("id", taskList.getId());
      intent.putExtra("title", taskList.getTitle());
      startActivity(intent);
    };
    cursor = TasksListMethods.getCursorTaskList(db);
    tasksList.clear();
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_ID));
      String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_NAME));
      TaskList taskList = new TaskList(id, title);
      tasksList.add(taskList);
    }
    Collections.sort(tasksList);
    adapter = new TasksListAdapter(this, listClickListener, tasksList);
    recyclerView.setAdapter(adapter);

    toolbar = findViewById(R.id.main_toolbar);
    setSupportActionBar(toolbar);

    buttonTasks = findViewById(R.id.button_tasks);
    buttonPlanned = findViewById(R.id.button_planned);

    Intent intent = new Intent(this, EditTaskActivity.class);
    intent.putExtra("previouslyPage", "MainActivity");
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          startActivity(intent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_sort) {
      Collections.reverse(tasksList);
      adapter = new TasksListAdapter(this, listClickListener, tasksList);
      recyclerView.setAdapter(adapter);
    } else if (item.getItemId() == R.id.menu_add_new_list) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      final EditText newList = new EditText(this);
      newList.setMaxLines(1);
      builder.setTitle(R.string.create_new_list)
              .setView(newList)
              .setPositiveButton(R.string.create, (dialog, which) ->
                      action(newList.getText().toString()));
      builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
      builder.show();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Действие для диалога. Используется для создания нового списка задач.
   *
   * @param title Заголовок списка задач.
   */
  private void action(String title) {
    TasksListMethods.createTaskList(db, title);
    cursor = TasksListMethods.getCursorTaskList(db);
    tasksList.clear();
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_ID));
      title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_NAME));
      TaskList taskList = new TaskList(id, title);
      tasksList.add(taskList);
    }
    Collections.sort(tasksList);
    adapter = new TasksListAdapter(this, listClickListener, tasksList);
    adapter.notifyDataSetChanged();
    recyclerView.setAdapter(adapter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    db.close();
    cursor.close();
  }

  /**
   * Переход на TasksActivity.
   *
   * @param view нажатия.
   */
  public void goToTasks(View view) {
    Intent intent = new Intent(this, TasksActivity.class);
    startActivity(intent);
  }

  /**
   * Переход на PlannedActivity.
   *
   * @param view нажатия.
   */
  public void goToPlanned(View view) {
    Intent intent = new Intent(this, PlannedActivity.class);
    startActivity(intent);
  }
}