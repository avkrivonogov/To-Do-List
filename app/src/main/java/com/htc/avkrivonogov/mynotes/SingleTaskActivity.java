package com.htc.avkrivonogov.mynotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.controllers.adapters.StepAdapter;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.data.TaskStepsMethods;
import com.htc.avkrivonogov.mynotes.data.TasksListMethods;
import com.htc.avkrivonogov.mynotes.models.TaskStep;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity задачи.
 */
public class SingleTaskActivity extends AppCompatActivity {

  private static final int REFRESH_TASK = 1;

  DatabaseHelper databaseHelper;
  SQLiteDatabase db;
  Cursor cursor;

  TextView titleView;
  TextView tasksListName;
  TextView creationDateView;
  TextView completeDateView;
  TextView reminderDateView;
  TextView reminderTimeView;
  TextView descriptionView;
  TextView completeStepView;
  ImageView imageView;
  RecyclerView recyclerView;

  int taskId;
  String title;
  String description;
  String image;
  String completeDate;
  String creationDate;
  String reminderDate;
  String reminderTime;
  String previouslyActivity = "";
  int listTaskId;
  int completeStatus;

  List<TaskStep> stepList = new ArrayList<>();

  Toolbar toolbar;


  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_task);

    databaseHelper = new DatabaseHelper(getApplicationContext());
    db = databaseHelper.getReadableDatabase();

    Bundle bundle = getIntent().getExtras();
    taskId = bundle.getInt("id");
    if (taskId != 0) {
      previouslyActivity = bundle.getString("previouslyActivity");
      if (previouslyActivity == null) {
        previouslyActivity = "";
      }
    }

    cursor = TaskMethods.getCursorFromId(db, taskId);
    if (cursor != null && cursor.moveToFirst()) {
      title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_NAME));
      description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_DESCRIPTION));
      image = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_IMAGE));
      creationDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_CREATION));
      completeDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_DATE));
      reminderDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_REMINDER_DATE));
      reminderTime =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_REMINDER_TIME));
      listTaskId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CATEGORY_ID));
      completeStatus =
              cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_STATUS));
    }

    toolbar = findViewById(R.id.edit_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_task);
    fab.setOnClickListener(v -> {
      Intent intent = new Intent(this, EditTaskActivity.class);
      intent.putExtra("id", taskId);
      intent.putExtra("previouslyActivity", "SingleTaskActivity");
      startActivity(intent);
    });

    titleView = findViewById(R.id.task_title);
    tasksListName = findViewById(R.id.tasks_list);
    descriptionView = findViewById(R.id.task_description);
    creationDateView = findViewById(R.id.creation_date);
    completeDateView = findViewById(R.id.complete_date);
    reminderDateView = findViewById(R.id.reminder_task_date);
    reminderTimeView = findViewById(R.id.reminder_task_time);
    completeStepView = findViewById(R.id.complete_step_view);
    imageView = findViewById(R.id.image);

    titleView.setText(title);
    creationDateView.setText(creationDate);
    if (completeDate != null) {
      completeDateView.setText(completeDate);
    } else {
      completeDateView.setText(R.string.no_choose_complete_date);
    }

    if (reminderDate != null) {
      reminderDateView.setText(reminderDate);
    } else {
      reminderDateView.setText(R.string.no_choose_reminder_date);
    }

    if (reminderTime != null) {
      reminderTimeView.setText(reminderTime);
    } else {
      reminderTimeView.setText(R.string.no_choose_reminder_time);
    }
    if (image != null) {
      Uri uri = Uri.parse(image);
      try {
        final InputStream imageStream = getContentResolver().openInputStream(uri);
        final Bitmap selectedImages = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(selectedImages);
        imageView.requestLayout();
        imageView.getLayoutParams().height =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        150, getResources().getDisplayMetrics());;
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    if (completeStatus == 1) {
      titleView.setPaintFlags(titleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    } else {
      titleView.setPaintFlags(titleView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
    }
    descriptionView.setText(description);

    String of = getString(R.string.of);
    String stepComplete = getString(R.string.step_complete);

    completeStepView.setText(TaskStepsMethods.getCompleteStepsTask(db, taskId) + " " + of
            + " " + TaskStepsMethods.getAllSteps(db, taskId) + " " + stepComplete);

    if (listTaskId != 0) {
      cursor = TasksListMethods.getCursorTaskListFromId(db, listTaskId);
      cursor.moveToFirst();
      tasksListName.setText(cursor.getString(0));
    } else {
      tasksListName.setText(R.string.list_no_choose);
    }

    if (bundle.getString("previouslyActivity") != null) {
      switch (bundle.getString("previouslyActivity")) {
        case "TasksActivity":
          getSupportActionBar().setTitle(R.string.tasks);
          break;
        case "TasksListActivity":
          getSupportActionBar().setTitle(cursor.getString(0));
          break;
        case "PlannedActivity":
          getSupportActionBar().setTitle(R.string.planned);
          break;
        default:
      }
    }

    recyclerView = findViewById(R.id.steps_list);
    if (!previouslyActivity.equals("EditTaskActivity")) {
      cursor = TaskStepsMethods.getCursorFromTaskId(db, taskId);
    } else {
      cursor = TaskStepsMethods.getCursorFromTaskTitle(db, title);
    }
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_STEP_ID));
      int taskId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TASK_ID));
      int stepStatus = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_STEP_COMPLETE));
      String stepContent =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_STEP_CONTENT));
      TaskStep step = new TaskStep(id, taskId, stepContent, stepStatus);
      stepList.add(step);
    }
    StepAdapter adapter = new StepAdapter(this, stepList);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_task, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_delete_task) {
      showDeleteTaskDialog();
//      if (!previouslyActivity.equals("EditTaskActivity")) {
//        finish();
//      } else {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//      }
    } else if (item.getItemId() == android.R.id.home) {
      if (!previouslyActivity.equals("EditTaskActivity")) {
        finish();
      } else {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    db.close();
    cursor.close();
  }

  /**
   * Диалог для удаления задачи.
   */
  private void showDeleteTaskDialog() {
    AlertDialog deleteTaskDialog = new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_task)
            .setMessage(R.string.confirm_message)
            .setPositiveButton(R.string.confirm, (dialog, which) -> {
              TaskStepsMethods.deleteStepByTaskId(db, taskId);
              TaskMethods.delete(db, taskId);
              Intent intent = new Intent();
              intent.putExtra("id", taskId);
              setResult(REFRESH_TASK, intent);
              finish();
            })
            .setNegativeButton(R.string.cancel, null)
            .create();
    deleteTaskDialog.show();
  }
}