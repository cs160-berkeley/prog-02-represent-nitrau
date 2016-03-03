package com.example.sarah.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Sarah on 2/29/2016.
 */
public class WatchListenerService extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event: dataEvents) {

            Log.d("[DEBUG] DeviceService - onDataChanged",
                    "Event received: " + event.getDataItem().getUri());

            String eventUri = event.getDataItem().getUri().toString();

            if (eventUri.contains ("/repdata")) {

                DataMapItem dataItem = DataMapItem.fromDataItem (event.getDataItem());
                String[] data = dataItem.getDataMap().getStringArray("REP");

                Log.d("[DEBUG] DeviceService - onDataChanged", "Sending timeline to the listener");

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("REP", data);
                startActivity(intent);

            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        intent.putExtra("LOCATION", value);
        Log.d("T", "Trying to start MainActivity");
        startActivity(intent);
    }
}
