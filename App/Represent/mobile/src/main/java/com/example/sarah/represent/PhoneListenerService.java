package com.example.sarah.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by Sarah on 2/29/2016.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String DETAIL = "/detail";
    private static final String RANDOM = "/random";
    private static CSVFile zips = null;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(DETAIL) ) {

            // Value contains the String we sent over in WatchToPhoneService
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            System.out.println(value);

            Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("DETAIL", value);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase(RANDOM)) {
            try {
                Intent intent = new Intent(getBaseContext(), CongressionalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (zips == null) {
                    InputStream in = getAssets().open("us_postal_codes.csv");
                    zips = new CSVFile(in);
                    zips.read();
                }
                int rand = new Random().nextInt(99999);
                while (!zips.find("" + rand)) {
                    rand = new Random().nextInt(99999);
                }
                intent.putExtra("ZIP_CODE", String.format("%05d", rand));
                intent.putExtra("ZIP_TRUE", true);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
