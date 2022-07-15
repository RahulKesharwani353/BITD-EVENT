package com.example.bitdevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );


        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        id = findViewById(R.id.signIn_id);
        email = findViewById(R.id.signIn_mail);
        password = findViewById(R.id.signIn_password);
        login_btn = findViewById(R.id.signIn_btn);

        ref = FirebaseDatabase.getInstance().getReference().child("students");

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login(){

        loadingBar.setTitle("Checking Credential");
        loadingBar.setMessage("please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id.getText().toString()).exists()){
                    idOrignal=snapshot.child(id.getText().toString()).child("id").getValue().toString();
                    passOrignal=snapshot.child(id.getText().toString()).child("password").getValue().toString();
                    emailOrignal = snapshot.child(id.getText().toString()).child("email").getValue().toString();


                    if (id.getText().toString().equals(idOrignal)&&password.getText().toString().equals(passOrignal)){
                        FireBaseSignIn();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please Check credentials again", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "User Does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }


                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void FireBaseSignIn() {
        String email_entered = email.getText().toString();
        String password_entered = password.getText().toString();


        mAuth.signInWithEmailAndPassword(email_entered, password_entered)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        loadingBar.dismiss();
                        String e = task.getException().toString();
                        Toast.makeText(MainActivity.this, "Credentials Does Not Match",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                });



    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
            loadingBar.dismiss();
            startActivity(new Intent(MainActivity.this,Dashboard.class));
            finishAffinity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


}