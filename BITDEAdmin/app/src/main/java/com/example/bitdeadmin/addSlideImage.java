package com.example.bitdeadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bitdeadmin.adapter.EventsAdapter;
import com.example.bitdeadmin.adapter.slideImageAdapter;
import com.example.bitdeadmin.model.EventModel;
import com.example.bitdeadmin.model.slideImageModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class addSlideImage extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    Button upload;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    ImageView imageView;

    private ImageView eventImage;
    private Uri ImageUri;
    private String downloadedUrl;
    private static int GalleryPick = 1;
    private StorageReference eventImgRef;
    private DatabaseReference ref;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slide_image);

        floatingActionButton = findViewById(R.id.add_SI_floatingBtn);
        upload = findViewById(R.id.slide_image_upload_btn);
        relativeLayout = findViewById(R.id.add_SI_relativeLayout);
        imageView = findViewById(R.id.add_SI_ImageView);
        recyclerView= findViewById(R.id.add_SI_recyclerView);


        eventImgRef = FirebaseStorage.getInstance().getReference().child("events");
        ref = FirebaseDatabase.getInstance().getReference().child("events");
        loadingBar = new ProgressDialog(this);

        imageView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GalleryPick);


        });


        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("slideImage").exists()){
                    FirebaseRecyclerOptions<slideImageModel> options =
                            new FirebaseRecyclerOptions.Builder<slideImageModel>()
                                    .setQuery(reference.child("slideImage"), slideImageModel.class)
                                    .build();

                    slideImageAdapter adapter = new slideImageAdapter(options,addSlideImage.this);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
                else Toast.makeText(addSlideImage.this, "Nothing To Show", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {

            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        floatingActionButton.setOnClickListener(v -> {
            relativeLayout.setVisibility(View.VISIBLE);
        });

        upload.setOnClickListener(v ->{
            if (ImageUri==null) {
                Toast.makeText(addSlideImage.this, "Please Select Image First", Toast.LENGTH_SHORT).show();
            }
            else {
                relativeLayout.setVisibility(View.GONE);
                submitData();
            }
                
    });
    }

    private void submitData() {
        loadingBar.setTitle("Uploading Data");
        loadingBar.setMessage("please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        @SuppressLint("SimpleDateFormat") String saveCurrentDate =
                new SimpleDateFormat("MM dd yyyy ").format(Calendar.getInstance().getTime());
        @SuppressLint("SimpleDateFormat") String saveCurrentTime=
                new SimpleDateFormat("HH:mm:ss a").format(Calendar.getInstance().getTime());
        String randomRequestId = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = eventImgRef.child(randomRequestId);

        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful())
                throw task.getException();
            downloadedUrl = filePath.getDownloadUrl().toString();
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                downloadedUrl = task.getResult().toString();

                HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("slideImgKey",randomRequestId);
                profileMap.put("slideImgLink", downloadedUrl);

                ref.child("slideImage").child(randomRequestId).
                        updateChildren(profileMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Intent intent = new Intent(addSlideImage.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                        loadingBar.dismiss();
                        Toast.makeText(addSlideImage.this, "Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                });

            }
            else {
                Toast.makeText(this, "can't Upload data", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }

        });

    }
}