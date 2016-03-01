package com.example.sarah.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sarah on 2/27/2016.
 */
public class CongressionalActivity extends AppCompatActivity {

    ViewPager viewPager;
    CustomSlideAdapter adapter;
    ArrayList<Representative> representativeArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("Congressional Activity started");

        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        //information to be retrieved from APIs in part C, this is just demo data
        representativeArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Representative rep = new Representative(R.drawable.rp_ca_13_lee_barbara, "Representative", "Barbara Lee", "Democratic", "someemail@gmail.com", "lee.house.gov", null);
            representativeArrayList.add(rep);
        }

        //send via phone to watch service

        Intent watch = new Intent(getBaseContext(), PhoneToWatchService.class);
        watch.putExtra("REPRESENTATIVES", representativeArrayList);
        startService(watch);

        viewPager = (ViewPager) findViewById(R.id.view_representatives);
        adapter = new CustomSlideAdapter(this, representativeArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(40);

    }

    public void onDetail(View view) {
        int current = viewPager.getCurrentItem();
        Representative currentRep = representativeArrayList.get(current);
        Intent intent = new Intent(getBaseContext(), DetailedActivity.class);

    }

}
