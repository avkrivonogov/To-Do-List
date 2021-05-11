package com.htc.avkrivonogov.mynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;
import com.htc.avkrivonogov.mynotes.data.TasksListMethods;
import com.htc.avkrivonogov.mynotes.models.Task;
import com.htc.avkrivonogov.mynotes.models.TaskList;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK = "Put";
    private static final int GALLERY_REQUEST = 2;
    public static final int REFRESH_TASK = 3;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    Bundle bundle;

//    EditText editTitle;
//    EditText editDescription;
    EditText editStep;
    Spinner listSpinner;
//    ImageButton reminderButton;
    ImageView imageView;
//    CheckBox checkComplete;
    List<String> stepList = new ArrayList<>();
    List<String> categoryList = new ArrayList<>();
    int id;
    int completeStep;
    Task task;

    List<TaskStep> taskStepList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

//        editTitle = findViewById(R.id.edit_task_title);
//        editDescription = findViewById(R.id.edit_description);
//        editStep = findViewById(R.id.edit_step_text);
        initializeTask();
        listSpinner = findViewById(R.id.edit_tasks_list_choose);

        cursor = TasksListMethods.getCursorTaskListTitle(db);
        categoryList.add("---");
        while (cursor.moveToNext()) {
            categoryList.add(cursor.getString(
                    cursor.getColumnIndex(DatabaseHelper.KEY_TASK_LIST_NAME)));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listSpinner.setAdapter(adapter);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            if (bundle.getInt("listId") != 0) {
                listSpinner.setSelection(bundle.getInt("listId"));
            }
        }

        initializeTaskFields();
        if (id != 0) {
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TASK_NAME));
            String description = cursor.getString(
                    cursor.getColumnIndex(DatabaseHelper.KEY_TASK_DESCRIPTION));
            int listTask = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CATEGORY_ID));

//            editTitle.setText(title);
//            editDescription.setText(description);

            listSpinner.setSelection(listTask);
        }

        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        reminderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        reminderButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
    }

    private void initializeTask() {
        Intent sentIntent = getIntent();
        if (sentIntent.hasExtra("taskId")) {
            cursor = TaskMethods.getCursorFromId(db, id);
            cursor.moveToFirst();
            task = TaskMethods.getTaskFromCursor(cursor);
            return;
        }

        task = new Task(0, "", 0);
        task.setCreation(LocalDateTime.now());
        task.setDescription("");
        task.setTaskListId(0);

        if (sentIntent.hasExtra("taskListId")) {
            int taskListId = sentIntent.getIntExtra("taskListId", 0);
            task.setTaskListId(Math.max(taskListId, 0));
        }
    }

    private void initializeTaskFields() {
        EditText et = findViewById(R.id.edit_task_title);
        et.setText(task.getTitle());

        et = findViewById(R.id.edit_description);
        et.setText(task.getDescription());

        LocalDate endDate = task.getCompleteDate();
        et = findViewById(R.id.edit_complete_date);
        et.setText(endDate != null
                ? endDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                : null);

        LocalDateTime reminderDateTime = task.getReminder();
        et = findViewById(R.id.edit_reminder_date_task);
        et.setText(reminderDateTime != null
                ? reminderDateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                : null);

        et = findViewById(R.id.edit_reminder_time_task);
        et.setText(reminderDateTime != null
                ? reminderDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                : null);

        Bitmap image = task.getImage();
        ImageView iv = findViewById(R.id.task_image);
        iv.setImageBitmap(image);
    }


    public static void start(Activity caller, Task task) {
        Intent intent = new Intent(caller, EditTaskActivity.class);
        if (task != null) {
            intent.putExtra(EXTRA_TASK, String.valueOf(task));
        }
        caller.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
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
            if (!checkBeforeSaving()) {
                return true;
            }
            Intent intent = new Intent();
            int taskId = task.getId();
            taskId = TaskMethods.addTask(this, task);
            intent.putExtra("taskId", taskId);
            setResult(REFRESH_TASK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkBeforeSaving() {
        boolean isDone = true;
        EditText editText = findViewById(R.id.edit_task_title);
        task.setTitle(editText.getText().toString());
        if ("".equals(task.getTitle())) {
            editText.setError(getResources().getString(R.string.task_title_empty));
            isDone = false;
        }

        editText = findViewById(R.id.edit_description);
        task.setDescription(editText.getText().toString());

        LocalDate endDate = task.getCompleteDate();
        editText = findViewById(R.id.edit_complete_date);
        if (endDate != null) {
            try {
                task.setCompleteDate(
                        LocalDate.parse(editText.getText().toString(), DateTimeFormatter.ofPattern("dd MM yyyy")));
            } catch (DateTimeException exception) {
                editText.setError(getResources().getString(R.string.end_date_error));
                isDone = false;
            }
        }

        editText = findViewById(R.id.edit_reminder_date_task);
        boolean isEmptyReminderDate = false;
        LocalDate reminderDate = null;
        if (!"".equals(editText.getText().toString())) {
            try {
                reminderDate = LocalDate.parse(editText.getText().toString(),
                        DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            } catch (DateTimeException exception) {
                editText.setError(getResources().getString(R.string.reminder_date_error));
                isDone = false;
            }
        } else {
            isEmptyReminderDate = true;
        }

        LocalDateTime reminderDateTime = task.getReminder();
        if (reminderDateTime != null && reminderDate != null) {
            LocalDateTime newReminderDateTime = LocalDateTime.of(reminderDate.getYear(),
                    reminderDate.getMonthValue(), reminderDate.getDayOfMonth(),
                    reminderDateTime.getHour(), reminderDateTime.getMinute(),
                    reminderDateTime.getSecond());
            task.setReminder(newReminderDateTime);
        }

        editText = findViewById(R.id.edit_reminder_time_task);
        boolean isEmptyReminderTime = false;
        LocalTime reminderTime = null;
        if (!"".equals(editText.getText().toString())) {
            try {
                reminderTime = LocalTime.parse(editText.getText().toString(),
                        DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (DateTimeException exception) {
                editText.setError(getResources().getString(R.string.reminder_time_error));
                isDone = false;
            }
        } else {
            isEmptyReminderTime = true;
        }

        reminderDateTime = task.getReminder();
        if (reminderDateTime != null && reminderTime != null) {
            LocalDateTime newReminderDateTime = LocalDateTime.of(reminderDateTime.getYear(),
                    reminderDateTime.getMonthValue(), reminderDateTime.getDayOfMonth(),
                    reminderTime.getHour(), reminderTime.getMinute(), reminderTime.getSecond());

            task.setReminder(newReminderDateTime);
        }

        if (isEmptyReminderDate && isEmptyReminderTime) {
            task.setReminder(null);
        } else if (isDone && isEmptyReminderDate) {
            editText = findViewById(R.id.edit_reminder_date_task);
            editText.setError(getResources().getString(R.string.reminder_date_error));
            isDone = false;
        } else if (isDone && isEmptyReminderTime) {
            editText = findViewById(R.id.edit_reminder_time_task);
            editText.setError(getResources().getString(R.string.reminder_time_error));
            isDone = false;
        }

        return isDone;
    }

    public void addStage(View view) {
        if (!editStep.getText().toString().equals("")) {
            stepList.add(editStep.getText().toString());
            editStep.setText("");
        }
    }

    public void addImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK & requestCode == GALLERY_REQUEST) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView = (ImageView) findViewById(R.id.task_image);
                        imageView.setImageBitmap(bitmap);

                        task.setImage(bitmap);
                        inputStream.close();
                    } catch (IOException e) {
                        Log.i("TAG", e.toString());
                    }
                    break;
            }
        }
    }

//    public void setTime(View v) {
//        new TimePickerDialog(this, t,
//                dateAndTime.get(Calendar.HOUR_OF_DAY),
//                dateAndTime.get(Calendar.MINUTE), true)
//                .show();
//    }
//
//    public void setDate(View view) {
//        new DatePickerDialog(this, t,
//                dateAndTime.get(Calendar.HOUR_OF_DAY),
//                dateAndTime.get(Calendar.MINUTE), true)
//                .show();
//    }
//
//    private void setInitialDateTime() {
//        currentDateTime.setText(DateUtils.formatDateTime(getApplicationContext(),
//                dateAndTime.getTimeInMillis(),
//                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
//                        | DateUtils.FORMAT_SHOW_TIME));
//    }
//
//    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
//            dateAndTime.set(Calendar.MINUTE, minute);
//            setInitialDateTime();
//        }
//    };
//    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            dateAndTime.set(Calendar.YEAR, year);
//            dateAndTime.set(Calendar.MONTH, monthOfYear);
//            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            setInitialDateTime();
//        }
//    };
}
