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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.data.TaskMethods;

public class SingleTaskActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    TextView titleView;
    TextView tasksListName;
    TextView creationDate;
    TextView completeDate;
    TextView reminderDate;
    TextView descriptionView;
    ImageView image;

    int taskId;
    String title;
    String description;
    String notificationDate;
    String notificationTime;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        Bundle bundle = getIntent().getExtras();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.steps_list);

        toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_task);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(SingleTaskActivity.this, EditTaskActivity.class);
            startActivity(intent);
        });

        titleView = findViewById(R.id.task_title);
        tasksListName = findViewById(R.id.tasks_list);
        descriptionView = findViewById(R.id.task_description);
        creationDate = findViewById(R.id.creation_date);
        completeDate = findViewById(R.id.complete_date);
        reminderDate = findViewById(R.id.reminder_task);
        image = findViewById(R.id.task_image);

        titleView.setText(title);
        descriptionView.setText(description);
        creationDate.setText((CharSequence) creationDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_task) {
            TaskMethods.delete(db, taskId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}