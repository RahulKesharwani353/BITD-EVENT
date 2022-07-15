package com.example.bitdeadmin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitdeadmin.adapter.EventsAdapter;
import com.example.bitdeadmin.model.EventModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventsPage extends AppCompatActivity {

    String category;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);
        category = getIntent().getStringExtra(Keys.selectedEvents);
        TextView t = findViewById(R.id.toolbar_title);
        t.setText(category);

        recyclerView = findViewById(R.id.event_recyclerView);
        reference = FirebaseDatabase.getInstance().getReference().child("events");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(category).exists()){
                    FirebaseRecyclerOptions<EventModel> options =
                            new FirebaseRecyclerOptions.Builder<EventModel>()
                                    .setQuery(reference.child(category).orderByChild("date"), EventModel.class)
                                    .build();

                    adapter = new EventsAdapter(options,EventsPage.this,category);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
                else Toast.makeText(EventsPage.this, "Nothing To Show", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}