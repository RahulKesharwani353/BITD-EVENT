package com.example.bitdevents;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {

    Button cultural,acad,sports,logout;
    String selected;
    private NavigationView nav_view;
    private List<SliderItem> sliderItems = new ArrayList<>();
    private SliderView sliderView;
    private SliderAdapterExample adapter;
    private DatabaseReference reference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        /*==================Tool Bar=====*/
   
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /*======All About Tool Bar***/

        cultural = findViewById(R.id.CE);
        acad = findViewById(R.id.AE);
        sports = findViewById(R.id.SE);
        sliderView = findViewById(R.id.imageSlider);
        nav_view = findViewById(R.id.nav_view);
        progressBar =findViewById(R.id.progressBarHome);

        reference = FirebaseDatabase.getInstance().getReference().child("events");


        /*----------------Image Slider-------------*/
        SliderAdapterExample adapterExample = new SliderAdapterExample(this);
        sliderView.setSliderAdapter(adapterExample);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        getList(adapterExample);

        /*------------------End------------------*/
        cultural.setOnClickListener(v -> {
            selected = "cultural";
            Intent i = new Intent(Dashboard.this,EventsPage.class);
            i.putExtra(Keys.selectedEvents,selected);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        acad.setOnClickListener(v -> {
            selected = "academics";
            Intent i = new Intent(Dashboard.this,EventsPage.class);
            i.putExtra(Keys.selectedEvents,selected);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        sports.setOnClickListener(v -> {
            selected = "sports";
            Intent i = new Intent(Dashboard.this,EventsPage.class);
            i.putExtra(Keys.selectedEvents,selected);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });



        /*======All About NAV Bar menus***/
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Dashboard.this,MainActivity.class));
                         finish();
                        break;
                    case R.id.nav_share:
//                        startActivity(new Intent(Dashboard.this,));
                        break;

                }

                return true;
            }
        });

    }

    private void getList(SliderAdapterExample adapterExample) {

        List<SliderItem> sliderItemList = new ArrayList<>();

        reference.child("slideImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    sliderItemList.add(new SliderItem(postSnapshot.child("slideImgLink").getValue().toString(),
                            postSnapshot.child("slideImgKey").getValue().toString()));
                }
                adapterExample.renewItems(sliderItemList);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}