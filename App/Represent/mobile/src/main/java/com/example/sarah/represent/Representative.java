package com.example.sarah.represent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.InputStream;
import java.net.URL;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Sarah on 2/28/2016.
 */
public class Representative {

    private String bioguideID;
    private String imgURL;
    private String title;
    private String name;
    private String party;
    private String email;
    private String website;
    private Bitmap bmp;
    private Tweet tweet;
    private String twitterId = "";
    private long tweetId;

    public Representative(String bioguideID, String title, String name, String party, String email, String website) {
        this.bioguideID = bioguideID;
        this.title = title;
        this.name = name;
        this.party = party;
        this.email = email;
        this.website = website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTweetId(long tweet) {
        this.tweetId = tweet;
    }

    public long getTweetId() {
        return tweetId;
    }

    public String getWebsite() {
        return website;
    }

    public String getBioguideID() {
        return bioguideID;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}