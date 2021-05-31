package com.htc.avkrivonogov.mynotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.TaskAdapter;
import com.htc.avkrivonogov.mynotes.controllers.comparators.SortCreationDate;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.data.TasksListMethods;
import com.htc.avkrivonogov.mynotes.models.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity списка задач.
 */
public class TasksListActivity extends AppCompatActivity {

  private static final int REFRESH_TASK_LIST = 1;

  DatabaseHelper databaseHelper;
  SQLiteDatabase db;
  Cursor cursor;

  RecyclerView recyclerView;
  List<Task> tasksList = new ArrayList<>();
  int tasksListId;
  TaskAdapter taskAdapter;
  TaskAdapter.OnTaskClickListener taskClickListener;
  FloatingActionButton fab;
  TextView listTitle;
  SortCreationDate sortCreationDate;
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
      intent.putExtra("previouslyActivity", "TasksActivity");
      startActivity(intent);
    };

    toolbar = findViewById(R.id.tasks_list_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    Bundle bundle = getIntent().getExtras();
    tasksListId = bundle.getInt("id");
//    ActionBar actionBar = getSupportActionBar();
//    actionBar.setTitle(bundle.getString("title"));
    listTitle = findViewById(R.id.tasks_list_title);
    listTitle.setText(bundle.getString("title"));

    cursor = TaskMethods.getCursorFromTaskList(db, tasksListId);
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
              completeDate,
              reminderDate,
              reminderTime,
              creation,
              listTaskId,
              complete, completeStep);
      tasksList.add(task);
    }
//    Collections.sort(tasksList);
    taskAdapter = new TaskAdapter(this, taskClickListener, tasksList);
    recyclerView.setAdapter(taskAdapter);

    fab = findViewById(R.id.tasks_list_fab_add);
    Intent intent = new Intent(this, EditTaskActivity.class);
    intent.putExtra("previouslyActivity", "TasksListActivity");
    fab.setOnClickListener(v -> startActivity(intent));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_tasks_list, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_delete_list) {
      showDeleteListDialog();
    } else if (item.getItemId() == R.id.menu_rename_list) {
      showRenameListDialog();
    } else if (item.getItemId() == android.R.id.home) {
      Intent refresh = new Intent(this, MainActivity.class);
      startActivity(refresh);
      finish();
    } else if (item.getItemId() == R.id.menu_sort_list) {
      Collections.reverse(tasksList);
      taskAdapter = new TaskAdapter(this, taskClickListener, tasksList);
      recyclerView.setAdapter(taskAdapter);
    }
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null) {
      return;
    }

    if (requestCode == 155 && resultCode == RESULT_OK) {
      int id = data.getIntExtra("id", 0);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    db.close();
    cursor.close();
  }

  /**
   * Диалог для удаления списка задач.
   */
  private void showDeleteListDialog() {
    AlertDialog deleteListDialog = new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_list)
            .setMessage(R.string.confirm_message)
            .setPositiveButton(R.string.confirm, (dialog, which) -> {
              TasksListMethods.delete(db, tasksListId);
              Intent intent = new Intent(getApplicationContext(), MainActivity.class);
              startActivity(intent);
            })
            .setNegativeButton(R.string.cancel, null)
            .create();
    deleteListDialog.show();
  }

  /**
   * Диалог для переименования списка задач.
   */
  private void showRenameListDialog() {
    final EditText renameList = new EditText(this);
    renameList.setMaxLines(1);
    AlertDialog renameListDialog = new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.rename_list)
            .setView(renameList)
            .setPositiveButton(R.string.rename, (dialog, which) -> {
              String newName = renameList.getText().toString();
              TasksListMethods.rename(db, tasksListId, newName);
//                      ActionBar actionBar = getSupportActionBar();
//                      if (actionBar != null) {
//                        actionBar.setTitle(newName);
//                      }
              listTitle.setText(newName);
              Intent intent = new Intent();
              intent.putExtra("id", tasksListId);
              setResult(REFRESH_TASK_LIST, intent);
            })
            .setNegativeButton(R.string.cancel, null)
            .create();
    renameList.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        renameListDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!"".contentEquals(s));
      }
    });
    renameListDialog.show();

    renameListDialog.getButton(
            AlertDialog.BUTTON_POSITIVE).setEnabled(!"".contentEquals(renameList.getText()));
  }
}