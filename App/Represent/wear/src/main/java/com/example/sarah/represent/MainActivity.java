package com.example.sarah.represent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.GridViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    ArrayList<Fragment> fragments = new ArrayList();
    Context ctx = this;

    GridViewPager pager;

    // a flag so the watch does not refresh the location randomly upon start
    private float lastX = -1;

    String tag = "WEAR:MAIN";
    String location;
    String repInfo;
    String imageURL = "http://theunitedstates.io/images/congress/225x275/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("T", "Watch: MainActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras =  intent.getExtras();

        fragments = new ArrayList<>();

        if (extras != null) {
            Log.d(tag, Arrays.toString(extras.keySet().toArray()));
            repInfo = extras.getString("JSON");
            try {
                JSONObject jsonRepInfo = new JSONObject(repInfo);
                JSONArray jsonReps = jsonRepInfo.getJSONArray("reps");
                for (int i = 0; i < jsonReps.length(); i++) {
                    JSONObject jsonRep = jsonReps.getJSONObject(i);
                    CustomFragment customFragment = new CustomFragment();
                    String imgURL = jsonRep.getString("img_url").replace("\\", "");
                    String title = jsonRep.getString("title");
                    String name = jsonRep.getString("name");
                    String party = jsonRep.getString("party");
                    String bioID = jsonRep.getString("bioguide_id");
                    customFragment.setArgs(bioID, imgURL, title, name, party);
                    customFragment.setBmp(bmpFromBytes(extras.getByteArray(bioID)));
                    fragments.add(customFragment);
                }
                location = jsonRepInfo.getString("county_name");
                JSONObject electionData = jsonRepInfo.getJSONObject("election_results");
                int romney = (int) electionData.getDouble("romney");
                int obama = (int) electionData.getDouble("obama");
                VoteFragment vf = new VoteFragment();
                vf.setArgs(location, romney, obama);
                fragments.add(vf);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (fragments.size() == 0) {
            fragments.add(new InitialFragment());
        }

        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GridPagerAdapter(this, getFragmentManager(), fragments));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void onSelect(View view) {
        int fragment = pager.getCurrentItem().y;
        String bioguideId = ((CustomFragment) fragments.get(fragment)).getBioID();
        Log.d(tag, "Selected " + bioguideId);
//        TextView textView = (TextView) view.findViewById(R.id.fragment_name);
//        String name = textView.getText().toString();
        Intent intent = new Intent(getBaseContext(), WatchToPhoneService.class);
        intent.putExtra("ACTION", "detail");
        intent.putExtra("REP", bioguideId);
        startService(intent);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastX < 0) {
            lastX = event.values[0];
        } else {
            if (Math.abs(event.values[0] - lastX) > 10) {
                Log.d(tag, "Sensor changed: " + event.values[0] + " last: " + lastX);
                lastX = event.values[0];
                Intent intent = new Intent(getBaseContext(), WatchToPhoneService.class);
                intent.putExtra("ACTION", "random");
                intent.putExtra("SHAKE", lastX);
                startService(intent);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("New intent started");
        super.onNewIntent(intent);
    }

    public Bitmap bmpFromBytes(byte[] bitmapdata) {
        if (bitmapdata == null) {
            Log.d(tag, "Byte array is null");
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
    }

}
