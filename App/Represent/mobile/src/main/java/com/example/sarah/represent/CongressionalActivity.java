package com.example.sarah.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Sarah on 2/27/2016.
 */
public class CongressionalActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    JSONObject jsonToWatch = new JSONObject();

    ViewPager viewPager;
    CustomSlideAdapter adapter;
    ArrayList<Representative> representativeArrayList;
    Location mLastLocation;
    String mLatitude;
    String mLongitude;
    String mZip;
    String mCounty;
    String mState;
    String mFullCounty;

    boolean connect = false;
    boolean zip = false;

    Context ctx = this;

    String countyFromLocation = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    String countyFromZip = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    String legislatorRequest = "https://congress.api.sunlightfoundation.com/legislators/locate?";
    String imageURL = "http://theunitedstates.io/images/congress/225x275/";

    private GoogleApiClient mGoogleApiClient;

    private static final String SUNLIGHT_KEY = "6cd60b07943c4e55a82c61044d8f65ee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("T", "Congressional Activity started for mobile");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        zip = extras.getBoolean("ZIP_TRUE");

        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_congressional);

        String location ="";

        TextView header = (TextView)findViewById(R.id.congressional_header);
        if (zip) {
            mZip = (String) extras.get("ZIP_CODE");
            legislatorRequest += "zip=" + mZip;
            Log.d("T", "Making request to " + legislatorRequest);
            legislatorRequest += "&apikey=" + SUNLIGHT_KEY;
            new getLegislatorsTask().execute(legislatorRequest);
            header.setText("Representatives for\n" + mZip);
        } else {
            connect = true;
            header.setText("Your local\n representatives");
        }

        viewPager = (ViewPager) findViewById(R.id.view_representatives);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onDetail(View view) {
        Log.d("T", "detail clicked");
        int current = viewPager.getCurrentItem();
        Representative currentRep = representativeArrayList.get(current);
        Log.d("T", "current rep: " + currentRep.getName());
        Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
        Log.d("T", "Starting detailed activity for " + currentRep.getBioguideID());
        intent.putExtra("DETAIL", currentRep.getBioguideID());
        intent.putExtra("IMAGE", currentRep.getBmp());
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("T", "Connected to Google Maps API");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null && connect) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongitude = String.valueOf(mLastLocation.getLongitude());
            Log.d("T", String.format("Latitude is: %s, longitude is: %s ", mLatitude, mLongitude));
            legislatorRequest += "latitude=" + mLatitude + "&longitude=" + mLongitude;
            Log.d("T", "Making request to " + legislatorRequest);
            legislatorRequest += "&apikey=" + SUNLIGHT_KEY;
            new getLegislatorsTask().execute(legislatorRequest);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class getLegislatorsTask extends AsyncTask<String, Void, ArrayList<Representative>> {

        @Override
        protected ArrayList<Representative> doInBackground(String... urls) {
            Log.d("T", "Getting legislators");
            StringBuilder result = new StringBuilder();
            ArrayList<Representative> legislators = new ArrayList<>();
            representativeArrayList = legislators;
            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                urlConnection.disconnect();

                JSONObject requestBody = new JSONObject(result.toString());
                JSONArray jsonLegislators = requestBody.getJSONArray("results");
                JSONArray jsonLegislatorsToWatch = new JSONArray();
                for (int i = 0; i < jsonLegislators.length(); i++) {
                    JSONObject legislator = jsonLegislators.getJSONObject(i);
                    JSONObject jsonLegislatorToWatch = new JSONObject();

                    String bioId = legislator.getString("bioguide_id");
                    String title = legislator.getString("title");
                    String name = String.format("%s %s", legislator.getString("first_name"), legislator.getString("last_name"));
                    String party = legislator.getString("party");
                    String email = legislator.getString("oc_email");
                    String website = legislator.getString("website");

                    final Representative rep = new Representative(bioId, title, name, party, email, website);


                    rep.setImgURL(getImgURL(bioId));

                    in = new URL(rep.getImgURL()).openStream();
                    Bitmap bmp = BitmapFactory.decodeStream(in);
                    rep.setBmp(bmp);

                    jsonLegislatorToWatch.put("title", rep.getTitle());
                    jsonLegislatorToWatch.put("name", rep.getName());
                    jsonLegislatorToWatch.put("party", rep.getParty());
                    jsonLegislatorToWatch.put("img_url", rep.getImgURL());
                    jsonLegislatorToWatch.put("bioguide_id", rep.getBioguideID());

                    if (legislator.has("twitter_id")) {
                        final String twitterId = legislator.getString("twitter_id");
                        rep.setTwitterId(twitterId);
                    }
                    legislators.add(rep);
                    jsonLegislatorsToWatch.put(jsonLegislatorToWatch);
                }
                jsonToWatch.put("reps", jsonLegislatorsToWatch);

                //get county data as JSON object
                if (zip) {
                    Log.d("CONGRESSIONAL", "Finding lat and long from zip: " + mZip);
                    url = new URL(getCountyURL());
                    Log.d("CONGRESSIONAL", "Making request to " + url.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    br = new BufferedReader(new InputStreamReader(in));
                    result = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    urlConnection.disconnect();

                    requestBody = new JSONObject(result.toString());
                    JSONArray zipAddress = requestBody.getJSONArray("results");
                    JSONObject zipGeometry = zipAddress.getJSONObject(0).getJSONObject("geometry");
                    JSONObject zipLocation = zipGeometry.getJSONObject("location");
                    mLatitude = zipLocation.getString("lat");
                    mLongitude = zipLocation.getString("lng");

                    zip = false;
                }
                url = new URL(getCountyURL());
                Log.d("CONGRESSIONAL", "Making request to " + url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader(new InputStreamReader(in));
                result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                urlConnection.disconnect();

                requestBody = new JSONObject(result.toString());
                JSONArray address = requestBody.getJSONArray("results");
                JSONArray addressComponents = address.getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < addressComponents.length(); i++) {
                    JSONObject components = addressComponents.getJSONObject(i);
                    JSONArray types = components.getJSONArray("types");
                    String mShortName = components.getString("short_name");
                    String mType = types.getString(0);
                    if (mType.equalsIgnoreCase("administrative_area_level_2")) {
                        mCounty = mShortName;
                    } else if (mType.equalsIgnoreCase("administrative_area_level_1")) {
                        mState = mShortName;
                    }
                }

                mFullCounty = String.format("%s, %s", mCounty, mState);
                Log.d("CONGRESSIONAL", "County: " + mFullCounty);

                InputStream stream = getAssets().open("newelectioncounty2012.json");
                Log.d("CONGRESSIONAL", "Opening election json file");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                String jsonString = new String(buffer, "UTF-8");
                JSONObject electionResults = new JSONObject(jsonString);
                JSONObject electionResult = electionResults.getJSONObject(mFullCounty);
                System.out.println(electionResult);
                jsonToWatch.put("county_name", mFullCounty);
                jsonToWatch.put("election_results", electionResult);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return legislators;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Representative> legislators) {
            adapter = new CustomSlideAdapter(ctx, legislators);
            viewPager.setAdapter(adapter);
            viewPager.setPageMargin(40);

            Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            watchIntent.putExtra("JSON", jsonToWatch.toString());
            String[] idPics = new String[legislators.size()];
            for (int i = 0; i < legislators.size(); i++) {
                idPics[i] = legislators.get(i).getBioguideID();
                Bitmap bmp = legislators.get(i).getBmp();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                watchIntent.putExtra(idPics[i], byteArray);
            }
            watchIntent.putExtra("idPics", idPics);
            Log.d("CONGRESSIONAL", "bioguid_ids: " + Arrays.toString(idPics));
            Log.d("CONGRESSIONAL", "Sending json to watch: " + jsonToWatch.toString());
            startService(watchIntent);
        }

    }

    private String getCountyURL() {
        if (zip) {
            return countyFromZip += mZip;
        } else {
            return countyFromLocation += mLatitude + ","+ mLongitude;
        }
    }

    public String getImgURL(String bioId) {
        return imageURL + bioId + ".jpg";
    }


}
