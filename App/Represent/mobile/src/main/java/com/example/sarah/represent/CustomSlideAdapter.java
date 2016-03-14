package com.example.sarah.represent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetView;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Sarah on 2/28/2016.
 */
public class CustomSlideAdapter extends PagerAdapter {

    Context ctx;
    private LayoutInflater layoutInflater;
    private ArrayList<Representative> representatives;

    private static final String TWITTER_KEY = "N09XRIsC35qBL9Qa9mToyzHGh";
    private static final String TWITTER_SECRET = "NiUfR5NrGemsYHpUr8uB4d0k9mw4afBHbiHxM2otHUmYG8FACW";

    public CustomSlideAdapter(Context ctx, ArrayList<Representative> representatives) {
        this.ctx = ctx;
        this.representatives = representatives;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideView = layoutInflater.inflate(R.layout.view_slide, container, false);

        ImageView portraitView = (ImageView) slideView.findViewById(R.id.slide_portrait);
        portraitView.setImageBitmap(representatives.get(position).getBmp());
//        portraitView.setImageBitmap(representatives.get(position).loadBitmapFromURl());

        TextView typeView = (TextView) slideView.findViewById(R.id.slide_type);
        typeView.setText(representatives.get(position).getTitle());

        TextView nameView = (TextView) slideView.findViewById(R.id.slide_name);
        nameView.setText(representatives.get(position).getName());

        TextView partyView = (TextView) slideView.findViewById(R.id.slide_party);
        String party = representatives.get(position).getParty();
        partyView.setText(party);
        if (party.equals("R")) {
            partyView.setTextColor(Color.parseColor("#891015"));
        } else if (!party.equals("D")) {
            partyView.setTextColor(Color.parseColor("#808080"));
        }

        TextView emailView = (TextView) slideView.findViewById(R.id.slide_email);
        emailView.setText(representatives.get(position).getEmail());

        TextView websiteView = (TextView) slideView.findViewById(R.id.slide_website);
        websiteView.setText(representatives.get(position).getWebsite());

        LinearLayout tweetLayout = (LinearLayout) slideView.findViewById(R.id.slide_tweet);
        loadTweet(representatives.get(position).getTwitterId(), tweetLayout);

        container.addView(slideView);
        return slideView;

    }

    private void loadTweet(final String twitterId, final LinearLayout layout) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(ctx, new Twitter(authConfig));

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, twitterId, 1, null, null, false, false, false, false, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        Tweet tweet = listResult.data.get(0);
                        CompactTweetView compactTweetView= new CompactTweetView(ctx, tweet);
//                        TweetView tweetView = new TweetView(ctx, tweet);
                        layout.addView(compactTweetView);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return representatives.size();
    }

    @Override
    public float getPageWidth(int position) {
        return 0.9f;
    }

}
