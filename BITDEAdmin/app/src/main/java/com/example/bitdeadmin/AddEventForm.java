package com.example.bitdeadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddEventForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Button date;
    private Button time;
    private Button cont1, cont2, done;
    private Spinner category;
    private static int GalleryPick = 1;
    private TextView num2,num1,num3;
    private String EnterDate="",EnterTime="",EnterCat="",EnterTitle="",EnterDescription="",EnterOrganiser,EnterGuest,EnterOpenFor;
    private EditText title,description,organise,openFor,guest;
    private LinearLayout layout1, layout2;
    private RelativeLayout layout3;
    private ImageView eventImage;
    private Uri ImageUri;
    private String downloadedUrl;
    private StorageReference eventImgRef;
    private DatabaseReference ref;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_form);

        eventImgRef = FirebaseStorage.getInstance().getReference().child("events");
        ref = FirebaseDatabase.getInstance().getReference().child("events");
        loadingBar = new ProgressDialog(this);

        category = findViewById(R.id.add_event_category);
        date = findViewById(R.id.seclectDate);
        time = findViewById(R.id.seletTime);
        title = findViewById(R.id.add_event_title);
        description = findViewById(R.id.add_event_description);
        organise = findViewById(R.id.add_event_organisation);
        openFor = findViewById(R.id.add_event_openFor);
        guest = findViewById(R.id.add_event_guest);
        eventImage = findViewById(R.id.event_image);


        cont1 = findViewById(R.id.contiu);
        cont2 = findViewById(R.id.contiu2);
        done = findViewById(R.id.contiu3);
        num2 = findViewById(R.id.s2);
        num1 = findViewById(R.id.S1);
        num3 = findViewById(R.id.S3);
        layout1 = findViewById(R.id.linerlayout1);
        layout2 = findViewById(R.id.linerlayout2);
        layout3 = findViewById(R.id.relativeLayout3);


        /*===================================list======================*/

        List<String> categories = new ArrayList<>();
        categories.add("cultural");
        categories.add("sports");
        categories.add("academics");
        /*----------------------list end----------------------*/
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(dataAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("choose category")) {

                } else {
                    String item = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(AddEventForm.this, item, Toast.LENGTH_SHORT).show();
                    EnterCat = item;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddEventForm.this, "please select category", Toast.LENGTH_SHORT).show();
            }
        });


        eventImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GalleryPick);


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {

            ImageUri = data.getData();
            eventImage.setImageURI(ImageUri);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        date.setOnClickListener(view -> showDatePickerDialog());
        time.setOnClickListener(v-> showTimePicker());
        cont1.setOnClickListener(view -> {
            /*=======================initialsing===============*/
            EnterTitle  = title.getText().toString();
            EnterDescription = description.getText().toString();

            if (EnterTitle.equals("") || EnterCat.equals("") || EnterDate.equals("") || EnterTime.equals("")|| EnterDescription.equals(""))
            {
                Toast.makeText(AddEventForm.this, "please enter all ", Toast.LENGTH_SHORT).show();
            }
            else {
                num1.setBackgroundResource(R.drawable.rectangle_non);
                num2.setBackgroundResource(R.drawable.rectangle_aktif);
                layout1.setVisibility(View.INVISIBLE);
                cont1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);
                cont2.setVisibility(View.VISIBLE);
            }

        });
        cont2.setOnClickListener(v->{
            /*=======================initialsing===============*/
            EnterOrganiser = organise.getText().toString();
            EnterOpenFor = openFor.getText().toString();
            EnterGuest = guest.getText().toString();

            if (EnterOrganiser.equals("") || EnterOpenFor.equals("") || EnterGuest.equals(""))
            {
                Toast.makeText(AddEventForm.this, "please enter all ", Toast.LENGTH_SHORT).show();
            }
            else {
                num2.setBackgroundResource(R.drawable.rectangle_non);
                num3.setBackgroundResource(R.drawable.rectangle_aktif);
                layout2.setVisibility(View.INVISIBLE);
                cont2.setVisibility(View.INVISIBLE);
                layout3.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        done.setOnClickListener(v -> {
            if (ImageUri==null)
                Toast.makeText(AddEventForm.this, "Please Select Image First", Toast.LENGTH_SHORT).show();
            else
            submitData();
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
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    throw task.getException();
                downloadedUrl = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddEventForm.this, "uploaded", Toast.LENGTH_SHORT).show();
                downloadedUrl = task.getResult().toString();
                HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("id",randomRequestId);
                profileMap.put("category",EnterCat);
                profileMap.put("title", EnterTitle);
                profileMap.put("description", EnterDescription);
                profileMap.put("date",EnterDate);
                profileMap.put("time", EnterTime);
                profileMap.put("Organisation", EnterOrganiser);
                profileMap.put("openFor",EnterOpenFor);
                profileMap.put("guest", EnterGuest);
                profileMap.put("img", downloadedUrl);

                ref.child(EnterCat).child(randomRequestId).
                        updateChildren(profileMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(AddEventForm.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                                loadingBar.dismiss();
                                Toast.makeText(AddEventForm.this, "Added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        });

            }
            else {
                Toast.makeText(this, "cant Upload data", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }

        });

    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog= new TimePickerDialog(this,this,Calendar.HOUR,Calendar.MINUTE,true);
        timePickerDialog.show();
    }


    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dat = i + "/" + (i1+1) + "/" + i2;
        date.setText(dat);
        EnterDate = i + "/" + (i1+1) + "/" + i2;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        String am_pm="AM";
        if(hourOfDay>11)
        {
            currentHour = hourOfDay - 12;
            am_pm= "PM";
        }
        else
        {
            currentHour = hourOfDay;
        }
        time.setText(currentHour+":"+minute+" "+am_pm);
        EnterTime = currentHour+":"+minute+" "+am_pm;

    }


}