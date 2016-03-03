package com.example.sarah.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by Sarah on 2/29/2016.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String DETAIL = "/detail";
    private static final String RANDOM = "/random";

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
            Intent intent = new Intent(getBaseContext(), CongressionalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ZIP_TRUE", true);
            int rand = new Random().nextInt(99999);
            intent.putExtra("ZIP_CODE", String.format("%05d", rand));
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
