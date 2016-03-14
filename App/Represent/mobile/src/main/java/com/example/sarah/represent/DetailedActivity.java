package com.example.sarah.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Sarah on 2/29/2016.
 */
public class DetailedActivity extends AppCompatActivity{

    private static final String SUNLIGHT_KEY = "6cd60b07943c4e55a82c61044d8f65ee";

    String imageURL = "http://theunitedstates.io/images/congress/225x275/";
    String genInfoRequest = "https://congress.api.sunlightfoundation.com/legislators?bioguide_id=";
    String committeeRequest = "https://congress.api.sunlightfoundation.com/committees?member_ids=";
    String billRequest = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=";
    String rep;
    Bitmap portrait = null;

    Context ctx = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        System.out.println("Detailed activity started");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rep = extras.getString("DETAIL");
            Log.d("DETAILED", "Activity started for rep " + rep);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        new getInfoTask().execute(rep);
    }

    private class getInfoTask extends AsyncTask<String, Void, ArrayList<JSONArray>> {

        String bioId;

        @Override
        protected ArrayList<JSONArray> doInBackground(String... params) {
            ArrayList<JSONArray> allInfo = new ArrayList<>();
            StringBuilder result = new StringBuilder();
            bioId = params[0];
            try {
                Log.d("DETAILED", "Setting picture");
                String picURL = imageURL + bioId + ".jpg";
                InputStream in = new URL(picURL).openStream();
                Bitmap bmp = BitmapFactory.decodeStream(in);
                portrait = bmp;

                URL url = new URL(constructURL(genInfoRequest, bioId));
                Log.d("DETAILED", "Making request to " + url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                urlConnection.disconnect();

                JSONObject requestBody = new JSONObject(result.toString());
                JSONArray jsonLegislators = requestBody.getJSONArray("results");
                allInfo.add(jsonLegislators);

                //get committees
                url = new URL(constructURL(committeeRequest, bioId));
                Log.d("DETAILED", "Making request to " + url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader(new InputStreamReader(in));
                result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                urlConnection.disconnect();
                requestBody = new JSONObject(result.toString());
                allInfo.add(requestBody.getJSONArray("results"));

                //get bills
                url = new URL(constructURL(billRequest, bioId));
                Log.d("DETAILED", "Making request to " + url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader(new InputStreamReader(in));
                result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                urlConnection.disconnect();
                requestBody = new JSONObject(result.toString());
                allInfo.add(requestBody.getJSONArray("results"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                return allInfo;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONArray> jsonArrays) {
            ImageView portraitView = (ImageView) findViewById(R.id.detailed_portrait);
            portraitView.setImageBitmap(portrait);
            setGenInfo(jsonArrays.get(0));
            setCommittees(jsonArrays.get(1));
            setBills(jsonArrays.get(2));
        }

    }
    /*
     * Constructs a one-query URL with the api key
     */
    public String constructURL(String base, String query) {
        return base + query + "&apikey=" + SUNLIGHT_KEY;
    }

    public void setGenInfo(JSONArray info) {
        try {
            JSONObject genInfo = info.getJSONObject(0);
            TextView partyName = (TextView) findViewById(R.id.detailed_party);
            TextView titleName = (TextView) findViewById(R.id.detailed_type);
            TextView name = (TextView) findViewById(R.id.detailed_name);
            TextView endDate = (TextView) findViewById(R.id.detailed_end_date);

            String party = genInfo.getString("party");
            if (party.equals("R")) {
                partyName.setText("Republican");
                partyName.setTextColor(Color.parseColor("#891015"));
            } else if (party.equals("D")) {
                partyName.setText("Democratic");
            } else {
                partyName.setTextColor(Color.parseColor("#808080"));
                partyName.setText(party);
            }
            titleName.setText(genInfo.getString("title"));
            name.setText(String.format("%s %s", genInfo.getString("first_name"), genInfo.getString("last_name")));
            endDate.setText(genInfo.getString("term_end"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCommittees(JSONArray committees) {
        LinearLayout committeeLayout = (LinearLayout) findViewById(R.id.commitees_layout);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(0, 10, 0, 0);
        JSONObject committee;
        TextView committeeView;

        try {
            for (int i = 0; i < committees.length(); i++) {
                committee = committees.getJSONObject(i);
                committeeView = new TextView(ctx);
                if (!committee.getBoolean("subcommittee")) {
                    committeeView.setTextColor(Color.BLACK);
                    committeeView.setText(committee.getString("name"));
                    committeeView.setLayoutParams(textLayoutParams);
                    committeeLayout.addView(committeeView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBills(JSONArray bills) {
        LinearLayout billLayout = (LinearLayout) findViewById(R.id.bills_layout);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView billView;
        JSONObject bill;
        textLayoutParams.setMargins(0, 10, 0, 0);
        try {
            int validBills = 0;
            for (int i = 0; i < bills.length(); i++) {
                if (validBills >= 5) {
                    break;
                }
                bill = bills.getJSONObject(i);
                if (!bill.isNull("short_title")) {
                    billView = new TextView(ctx);
                    billView.setTextColor(Color.BLACK);
                    billView.setText(bill.getString("short_title"));
                    billView.setLayoutParams(textLayoutParams);
                    billLayout.addView(billView);
                    validBills++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
