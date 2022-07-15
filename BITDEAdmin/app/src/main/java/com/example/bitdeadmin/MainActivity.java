package com.example.bitdeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    TextInputEditText id,password,email;
    String idOrignal,passOrignal,emailOrignal;
    Button login_btn;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.signIn_id);
        password = findViewById(R.id.signIn_password);
        login_btn = findViewById(R.id.signIn_btn);

        ref = FirebaseDatabase.getInstance().getReference().child("admin");

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().equals("")||password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "enter all details first", Toast.LENGTH_SHORT).show();
                }
                else
                login();
            }
        });

    }

    public void login(){


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id.getText().toString()).exists()){
                    idOrignal=snapshot.child(id.getText().toString()).child("id").getValue().toString();
                    passOrignal=snapshot.child(id.getText().toString()).child("password").getValue().toString();


                    if (id.getText().toString().equals(idOrignal)&&password.getText().toString().equals(passOrignal)){
                        Toast.makeText(MainActivity.this, "welcome", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,Dashboard.class));
                        finish();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "failed, Try again", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "User Does not exist", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}