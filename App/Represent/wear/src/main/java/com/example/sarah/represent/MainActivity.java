package com.example.sarah.represent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    // a flag so the watch does not refresh the location randomly upon start
    private float lastX = -1;

    String repArea = "13th District of California";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("T", "Watch: MainActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras =  intent.getExtras();

        ArrayList<Fragment> fragments = new ArrayList<>();

        if (extras != null) {
            repArea = extras.getString("LOCATION");
            fragments.add(CardFragment.create("Representatives for", repArea));
            System.out.println(repArea);
        }


        for (int i = 0; i < 3; i++) {
            CustomFragment customFragment = new CustomFragment();
            customFragment.setArgs(R.drawable.rp_ca_13_lee_barbara, "Representative", "Barbara Lee", "D");
            fragments.add(customFragment);
        }

        VoteFragment vf = new VoteFragment();
        vf.setArgs(repArea, 89, 9);
        fragments.add(vf);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GridPagerAdapter(this, getFragmentManager(), fragments));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void onSelect(View view) {
        TextView textView = (TextView)view.findViewById(R.id.fragment_name);
        String name = textView.getText().toString();
        Intent intent = new Intent(getBaseContext(), WatchToPhoneService.class);
        intent.putExtra("ACTION", "detail");
        intent.putExtra("REP", name);
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
                Log.d("T", "Sensor changed: " + event.values[0] + " last: " + lastX);
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
}
