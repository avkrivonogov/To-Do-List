package com.htc.avkrivonogov.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import com.htc.avkrivonogov.mynotes.models.Task;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK = "Put";
    EditText taskTitle;
    ImageButton reminderButton;

    List<TaskStep> taskStepList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reminderButton = (ImageButton) findViewById(R.id.reminder_button);
        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        reminderButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }

    public static void start(Activity caller, Task task) {
        Intent intent = new Intent(caller, EditTaskActivity.class);
        if (task != null) {
            intent.putExtra(EXTRA_TASK, String.valueOf(task));
        }
        caller.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//            case R.id.menu_done:
//        }
//        return super.onOptionsItemSelected(item);
//    }
}