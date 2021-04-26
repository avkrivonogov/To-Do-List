package com.htc.avkrivonogov.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htc.avkrivonogov.mynotes.models.TaskStep;

import java.util.ArrayList;

public class SingleTask extends AppCompatActivity {

    ArrayList<TaskStep> taskSteps = new ArrayList<>();
    ArrayAdapter<TaskStep> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.steps_list);
        arrayAdapter = new ArrayAdapter<TaskStep>();
    }

//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
//    fab.setOnCLickListener (new View.OnClickListener() {
//       @Override
//       public void onClick(View view) {
//
//        }
//    });
}