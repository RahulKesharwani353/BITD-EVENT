package com.example.bitdeadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    Button cultural,acad,sports,addEvent,addStudent,editStudent;
    String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cultural = findViewById(R.id.CE);
        acad = findViewById(R.id.AE);
        sports = findViewById(R.id.SE);
        editStudent = findViewById(R.id.editStudentBtn);
        addEvent = findViewById(R.id.addEventBtn);
        addStudent = findViewById(R.id.addStudentBtn);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,AddStudentDetails.class));
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,AddEventForm.class));
            }
        });

        editStudent.setOnClickListener(v ->{
            startActivity(new Intent(Dashboard.this,addSlideImage.class));
        });

        cultural.setOnClickListener(v -> {
            selected = "cultural";
            Intent i = new Intent(Dashboard.this,EventsPage.class);
            i.putExtra(Keys.selectedEvents,selected);
            startActivity(i);
        });

        acad.setOnClickListener(v -> {
            selected = "academics";
            Intent i = new Intent(Dashboard.this,EventsPage.class);
            i.putExtra(Keys.selectedEvents,selected);
            startActivity(i);
        });

        sports.setOnClickListener(v -> {
            selected = "sports";
            Intent i = new Intent(Dashboard.this,EventsPage.class);
            i.putExtra(Keys.selectedEvents,selected);
            startActivity(i);
        });

    }
    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit and logout", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}