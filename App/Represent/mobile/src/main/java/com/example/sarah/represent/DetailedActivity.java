package com.example.sarah.represent;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Sarah on 2/29/2016.
 */
public class DetailedActivity extends AppCompatActivity{

    Representative rep;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        System.out.println("Detailed activity started");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        rep = (Representative) extras.get("REPRESENTATIVE");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
    }
}
