package com.htc.avkrivonogov.mynotes;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.data.TaskStepsMethods;
import com.htc.avkrivonogov.mynotes.data.TasksListMethods;
import com.htc.avkrivonogov.mynotes.models.Task;
import com.htc.avkrivonogov.mynotes.receiver.AlarmReceiver;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Activity редактирования или добавления здаачи.
 */
public class EditTaskActivity extends AppCompatActivity {
  private static final int REFRESH_TASK = 1;
  DatabaseHelper databaseHelper;
  SQLiteDatabase db;
  Cursor cursor;

  int id = 0;
  int completeStatus;
  int completeStep;
  String reminderDate;
  String reminderTime;
  String dateComplete;
  EditText editTitle;
  EditText editDescription;
  EditText editStep;
  TextView creationDateView;
  TextView reminderDateView;
  TextView reminderTimeView;
  Uri selectedImage;
  Spinner listSpinner;
  DatePicker setCompleteDate;
  CheckBox checkCompleteDate;
  Bundle bundle;
  List<String> stepList = new ArrayList<>();
  List<String> listTask = new ArrayList<>();
  Toolbar toolbar;

  Calendar calendar = Calendar.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_task);

    databaseHelper = new DatabaseHelper(getApplicationContext());
    db = databaseHelper.getReadableDatabase();

    toolbar = findViewById(R.id.edit_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    editTitle = findViewById(R.id.edit_task_title);
    editDescription = findViewById(R.id.edit_description);
    editStep = findViewById(R.id.edit_step);
    creationDateView = findViewById(R.id.edit_creation_date);
    checkCompleteDate = findViewById(R.id.edit_check_complete_date);
    setCompleteDate = findViewById(R.id.edit_set_complete_date);
    reminderDateView = findViewById(R.id.edit_reminder_date_task);
    reminderTimeView = findViewById(R.id.edit_reminder_time_task);
    listSpinner = findViewById(R.id.edit_tasks_list_choose);

    cursor = TasksListMethods.getCursorTaskListTitle(db);
    String info = getString(R.string.task_title_empty);
    listTask.add(info);
    while (cursor.moveToNext()) {
      listTask.add(cursor.getString(
                    cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_NAME)));
    }
    ArrayAdapter<String> adapter =
            new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTask);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    listSpinner.setAdapter(adapter);

    bundle = getIntent().getExtras();
    if (bundle != null) {
      id = bundle.getInt("id");
      if (bundle.getInt("listId") != 0) {
        listSpinner.setSelection(bundle.getInt("listId"));
      }
    }
    if (id != 0) {
      cursor = TaskMethods.getCursorFromId(db, id);
      cursor.moveToFirst();
      String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_NAME));
      String description =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_DESCRIPTION));
      String image = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_IMAGE));
      int listTasksId =
              cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CATEGORY_ID));
      dateComplete =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_DATE));
      String creationDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_CREATION));
      String strReminderDate =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_REMINDER_DATE));
      String strReminderTime =
              cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_REMINDER_TIME));
      completeStatus = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_STATUS));
      completeStep = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_COMPLETE_STEPS));
      if (dateComplete != null) {
        checkCompleteDate.setChecked(true);
        setCompleteDate.setVisibility(View.VISIBLE);
      }

      editTitle.setText(title);
      editDescription.setText(description);
      creationDateView.setText(creationDate);
      reminderDateView.setText(strReminderDate);
      reminderTimeView.setText(strReminderTime);
      listSpinner.setSelection(listTasksId);
      if (selectedImage != null) {
        selectedImage = Uri.parse(image);
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_edit_task, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    } else if (item.getItemId() == R.id.menu_action_done) {
      saveChange();
    } else if (item.getItemId() == R.id.menu_add_image) {
      Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intent.setType("image/*");
      startActivityForResult(intent, 1);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    db.close();
    cursor.close();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if ((requestCode == 1) && (resultCode == RESULT_OK)) {
      selectedImage = data.getData();
    }
  }

  /**
   * Сохранение данных задачи после создания или изменения.
   */
  private void saveChange() {
    if (editTitle.getText().toString().equals("")) {
      editTitle.setError(getResources().getString(R.string.task_title_empty));
    } else {
      String title = editTitle.getText().toString();
      String description = null;
      if (!editDescription.getText().toString().equals("")) {
        description = editDescription.getText().toString();
      }
      String image = null;
      if (selectedImage != null) {
        image = selectedImage.toString();
      }
      String creationDate = LocalDateTime.now().format(
              DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
      if (checkCompleteDate.isChecked()) {
        int day = setCompleteDate.getDayOfMonth();
        String strDay = String.valueOf(day);
        if (day < 10) {
          strDay = "0".concat(strDay);
        }
        int month = setCompleteDate.getMonth() + 1;
        String strMonth = String.valueOf(month);
        if (month < 10) {
          strMonth = "0".concat(strMonth);
        }
        dateComplete = strDay + "." + strMonth + "." + setCompleteDate.getYear();
      }

      int listTask = listSpinner.getSelectedItemPosition();
      Task task;
      if (bundle.getString("previouslyActivity") != null
              && bundle.getString("previouslyActivity").equals("SingleTaskActivity")) {
        task = new Task(id, title, description, image, creationDate, dateComplete,
                reminderDate, reminderTime, listTask, completeStatus, completeStep);
        TaskMethods.updateTask(db, task);
        TaskStepsMethods.insert(db, task.getId(), stepList);
      } else {
        task = new Task(title, description, image, creationDate, dateComplete,
                reminderDate, reminderTime, listTask, 0, 0);
        TaskMethods.addTask(db, task);
        id = TaskMethods.taskLastId(db);
        TaskStepsMethods.insert(db, id, stepList);
      }

      Intent intent = new Intent();
      intent.putExtra("id", id);
      setResult(REFRESH_TASK, intent);
      finish();

      if (reminderDate != null && reminderTime != null) {
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String reminder = reminderTime +  ":00" + reminderDate;
        try {
          date = dateFormat.parse(reminder);
        } catch (ParseException e) {
          e.printStackTrace();
        }

        Intent reminderIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        reminderIntent.putExtra("taskId", id);
        reminderIntent.putExtra("title", task.getTitle());
        if (task.getDescription() != null) {
          reminderIntent.putExtra("description", task.getDescription());
        }
        if (task.getImage() != null) {
          reminderIntent.putExtra("image", task.getImage());
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, reminderIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, date.getTime(), pendingIntent);
      }
    }
  }

  /**
   * Добавление шага задачи.
   *
   * @param view нажатия.
   */
  public void addStep(View view) {
    if (!editStep.getText().toString().equals("")) {
      stepList.add(editStep.getText().toString());
      editStep.setText("");
    }
  }

  /**
   * Добавление даты завершения задачи.
   *
   * @param view нажатия.
   */
  public void addCompleteDate(View view) {
    if (checkCompleteDate.isChecked()) {
      setCompleteDate.setVisibility(View.VISIBLE);
    } else {
      setCompleteDate.setVisibility(View.INVISIBLE);
    }
  }

  /**
   * Установка времени напоминания.
   *
   * @param view нажатия.
   */
  public void reminderTime(View view) {
    MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(R.string.select_reminder_time)
            .build();
    timePicker.addOnPositiveButtonClickListener(v -> {
      int hour = timePicker.getHour();
      int minute = timePicker.getMinute();
      String strHour = String.valueOf(hour);
      if (hour < 10) {
        strHour = "0".concat(strHour);
      }
      String strMinute = String.valueOf(minute);
      if (minute < 10) {
        strMinute = "0".concat(strMinute);
      }
      reminderTime = strHour + ":" + strMinute;
      reminderTimeView.setText(reminderTime);
    });
    timePicker.show(getSupportFragmentManager(), null);
  }

  /**
   * Установка даты напоминания.
   *
   * @param view нажатия.
   */
  public void reminderDate(View view) {
    DatePickerDialog.OnDateSetListener d = (view1, year, month, dayOfMonth) -> {
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      reminderDateView.setText(DateUtils.formatDateTime(getApplicationContext(),
              calendar.getTimeInMillis(),
              DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
      reminderDate = (DateUtils.formatDateTime(getApplicationContext(),
              calendar.getTimeInMillis(),
              DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    };
    new DatePickerDialog(EditTaskActivity.this, d,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
            .show();
  }

  public void delTimeReminder(View view) {
    reminderTime = null;
    reminderTimeView.setText(R.string.no_choose_reminder_time);
  }

  public void delCompleteDate(View view) {
  }

  public void delDateReminder(View view) {
    reminderDate = null;
    reminderDateView.setText(R.string.no_choose_reminder_date);
  }
}
