package com.example.bitdeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddStudentDetails extends AppCompatActivity {

    private EditText email,name,password,id;
    private FirebaseAuth mAuth;
    private DatabaseReference studRef;
    private ProgressDialog loadingBar;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_details);

        loadingBar = new ProgressDialog(this);

        mAuth =  FirebaseAuth.getInstance();
        studRef = FirebaseDatabase.getInstance().getReference().child("students");


        email = findViewById(R.id.student_register_email);
        password = findViewById(R.id.student_register_password);
        id = findViewById(R.id.student_register_id);
        name = findViewById(R.id.student_register_name);
        Button submit = findViewById(R.id.add_student);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")||password.getText().toString().equals("")||
                id.getText().toString().equals("")||email.getText().toString().equals(""))
                Toast.makeText(AddStudentDetails.this, "Enter All details first", Toast.LENGTH_SHORT).show();
                else if (password.getText().toString().length()<6){
                    Toast.makeText(AddStudentDetails.this, "Password must be of minimum 6 numbers", Toast.LENGTH_SHORT).show();
                }
                else{
                    signUp();
                }


            }
        });
    }

    private void signUp() {
        loadingBar.setTitle("Uploading Data");
        loadingBar.setMessage("please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("id",id.getText().toString());
        profileMap.put("name",name.getText().toString());
        profileMap.put("email",email.getText().toString());
        profileMap.put("password",password.getText().toString());



     /*--------------------firebase signUp------------------*/
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            studRef.child(id.getText().toString()).
                                    updateChildren(profileMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Intent intent = new Intent(AddStudentDetails.this, Dashboard.class);
                                    loadingBar.dismiss();
                                    startActivity(intent);
                                    finish();
//                progressDialog.dismiss();
                                }
                                else {
                                    Toast.makeText(AddStudentDetails.this, "failed, try again..!!", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            });

                            Toast.makeText(AddStudentDetails.this, "Student Added To database", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AddStudentDetails.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }


}