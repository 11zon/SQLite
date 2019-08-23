package com.elevenzon.sqlite;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<NoteModel> arrayList;
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    DatabaseHelper database_helper;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        actionButton = (FloatingActionButton) findViewById(R.id.add);
        database_helper = new DatabaseHelper(this);
        displayNotes();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    //display notes list
    public void displayNotes() {
        arrayList = new ArrayList<>(database_helper.getNotes());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NotesAdapter adapter = new NotesAdapter(getApplicationContext(), this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    //display dialog
    public void showDialog() {
        final EditText title, des;
        Button submit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        submit = (Button) dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Title");
                }else if(des.getText().toString().isEmpty()) {
                    des.setError("Please Enter Description");
                }else {
                    database_helper.addNotes(title.getText().toString(), des.getText().toString());
                    dialog.cancel();
                    displayNotes();
                }
            }
        });
    }
}