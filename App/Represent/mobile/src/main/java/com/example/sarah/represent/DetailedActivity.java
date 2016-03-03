package com.example.sarah.represent;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sarah on 2/29/2016.
 */
public class DetailedActivity extends AppCompatActivity{

    String rep;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        System.out.println("Detailed activity started");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rep = extras.getString("DETAIL");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        //Demo information set, to be replaced with API calls

        LinearLayout committeeLayout = (LinearLayout) findViewById(R.id.commitees_layout);

        TextView sampleCommittee = new TextView(this);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(0, 10, 0, 0);
        sampleCommittee.setLayoutParams(textLayoutParams);
        sampleCommittee.setText("Appropriations Committee");

        committeeLayout.addView(sampleCommittee, 1);

        LinearLayout billLayout = (LinearLayout) findViewById(R.id.bills_layout);
        TextView sampleBill = new TextView(this);
        sampleBill.setLayoutParams(textLayoutParams);
        sampleBill.setText("H.R. 3712: Improving Access to Mental Health Act (Oct 8, 2015)");

        billLayout.addView(sampleBill, 1);

    }
}
