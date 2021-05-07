package com.htc.avkrivonogov.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.data.DatabaseHelper;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

import java.util.ArrayList;

public class SingleTask extends AppCompatActivity {

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String title;
    String content;
    String notificationDate;
    String notificationTime;

    ArrayList<TaskStep> taskSteps = new ArrayList<>();
    ArrayAdapter<TaskStep> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);

        sqlHelper = new DatabaseHelper(getApplicationContext());
        db = sqlHelper.getReadableDatabase();

        Bundle bundle = getIntent().getExtras();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.steps_list);
//        arrayAdapter = new ArrayAdapter<TaskStep>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleTask.this, EditTaskActivity.class);
                startActivity(intent);
            }
        });

    }


}