package com.example.sarah.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGo(View view) {
        EditText zipText = (EditText)findViewById(R.id.ziptext);
        String zipCode = zipText.getText().toString();
        if (!zipCode.matches("")) {
            //get congressional representatives frm API
            System.out.println("Zip code picked");
            Intent intent = new Intent(getBaseContext(), CongressionalActivity.class);
            intent.putExtra("ZIP_CODE", zipCode);
            intent.putExtra("ZIP_TRUE", true);
            startActivity(intent);
        }
    }

    public void onSelectLocation(View view) {
        //get congressional representatives from API
        //get location from API
        System.out.println("Select location clicked");
        Intent intent = new Intent(getBaseContext(), CongressionalActivity.class);
        intent.putExtra("ZIP_TRUE", false);
        startActivity(intent);
    }
}
