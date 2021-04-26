package com.htc.avkrivonogov.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.htc.avkrivonogov.mynotes.models.Task;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

import java.util.List;

public class EditTask extends AppCompatActivity {

    private static final String EXTRA_TASK = "EditTask.EXTRA_TASK";

    List<TaskStep> taskStepList;

    Toolbar toolbar;

    EditText taskTitle;

    public static void start(Activity caller, Task task) {
        Intent intent = new Intent(caller, EditTask.class);
        intent.putExtra(EXTRA_TASK, String.valueOf(task));
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(getString(R.string.edit_task));

        taskTitle.findViewById(R.id.task_title);
    }

//    public static void start(Activity caller, Task task) {
//        Intent intent = new Intent(caller, EditTask.class);
//        if (task != null) {
//            intent.putExtra(EXTRA_TASK, task);
//        }
//        caller.startActivity(intent);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_done:

        }
        return super.onOptionsItemSelected(item);
    }
}