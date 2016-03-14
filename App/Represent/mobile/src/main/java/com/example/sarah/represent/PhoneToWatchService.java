package com.example.sarah.represent;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Sarah on 2/27/2016.
 */
public class PhoneToWatchService extends Service {

    String tag = "MOBILE:TOWATCH";
    private GoogleApiClient mApiClient;
    final Service _this = this;

    @Override
    public void onCreate() {
        super.onCreate();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnectionSuspended(int i) {
                    }

                    @Override
                    public void onConnected(Bundle connectionHint) {

                    }
                })
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "Started");
        Bundle extras = intent.getExtras();

        final String repdata = extras.getString("JSON");
        String[] idPics = extras.getStringArray("idPics");
        Log.d(tag, "Received ids: " + Arrays.toString(idPics));

        final PutDataMapRequest putRequest = PutDataMapRequest.create("/send_data");
        final DataMap map = putRequest.getDataMap();

        map.putString("repdata", repdata);
        map.putStringArray("bioIds", idPics);
        map.putLong("time", System.currentTimeMillis());


        for (String s : idPics) {
            byte[] idPic = extras.getByteArray(s);
            Log.d(tag, "" + idPic.length);
            map.putAsset(s, Asset.createFromBytes(idPic));
        }

        final PutDataRequest dataRequest = putRequest.asPutDataRequest();
        Log.d(tag, Arrays.toString(map.keySet().toArray()));

        new Thread(new Runnable () {

            @Override
            public void run() {
                mApiClient.connect();
                Log.d("T", "" + mApiClient.isConnected());
                sendData(dataRequest);
//                sendMessage("/reps", repdata);
                _this.stopSelf();
            }
        }).start();
        return START_STICKY;
    }

    private void sendData(final PutDataRequest dataRequest) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Sending data");
                Wearable.DataApi.putDataItem(mApiClient, dataRequest);
                Log.d(tag, dataRequest.toString());
                mApiClient.connect();
            }

        }).start();
    }

    private void sendMessage( final String path, final String text) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        Log.d(tag, "Trying to send message");
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                Log.d("MOBILE:TOWATCH", "Nodes:" + Arrays.toString(nodes.getNodes().toArray()));
                for (Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes()).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mApiClient.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
