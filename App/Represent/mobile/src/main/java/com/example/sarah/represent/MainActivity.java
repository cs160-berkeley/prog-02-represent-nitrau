package com.example.sarah.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "N09XRIsC35qBL9Qa9mToyzHGh";
    private static final String TWITTER_SECRET = "NiUfR5NrGemsYHpUr8uB4d0k9mw4afBHbiHxM2otHUmYG8FACW";



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
        intent.putExtra("LOCATION", "-58.596° W, 60° N");
        startActivity(intent);
    }
}
