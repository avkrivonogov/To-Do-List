package com.htc.avkrivonogov.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.data.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHandler databaseHandler;
    private Button buttonTasks;
    private Button buttonPlanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.getWritableDatabase();
        databaseHandler.getReadableDatabase();

        buttonTasks = (Button) findViewById(R.id.button_tasks);
        buttonTasks.setOnClickListener(this);
        buttonPlanned = (Button) findViewById(R.id.button_planned);
        buttonPlanned.setOnClickListener(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditTask.start(MainActivity.this, null);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_planned:
                Intent intent1 = new Intent(MainActivity.this, Planned.class);
                startActivity(intent1);
                break;
            case R.id.button_tasks:
                Intent intent2 = new Intent(MainActivity.this, Tasks.class);
                startActivity(intent2);
                break;
        }
    }
}