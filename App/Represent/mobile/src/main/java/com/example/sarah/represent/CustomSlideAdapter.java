package com.example.sarah.represent;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sarah on 2/28/2016.
 */
public class CustomSlideAdapter extends PagerAdapter {

    Context ctx;
    private LayoutInflater layoutInflater;
    private ArrayList<Representative> representatives;

    public CustomSlideAdapter(Context ctx, ArrayList<Representative> representatives) {
        this.ctx = ctx;
        this.representatives = representatives;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideView = layoutInflater.inflate(R.layout.view_slide, container, false);

        ImageView portraitView = (ImageView) slideView.findViewById(R.id.slide_portrait);
        portraitView.setImageResource(representatives.get(position).getPortrait());

        TextView typeView = (TextView) slideView.findViewById(R.id.slide_type);
        typeView.setText(representatives.get(position).getType());

        TextView nameView = (TextView) slideView.findViewById(R.id.slide_name);
        nameView.setText(representatives.get(position).getName());

        TextView partyView = (TextView) slideView.findViewById(R.id.slide_party);
        partyView.setText(representatives.get(position).getParty());

        TextView emailView = (TextView) slideView.findViewById(R.id.slide_email);
        emailView.setText(representatives.get(position).getEmail());

        TextView websiteView = (TextView) slideView.findViewById(R.id.slide_website);
        websiteView.setText(representatives.get(position).getWebsite());

        //tweets set to default image for Part B, will be retrieved and stored as XML in part C

        container.addView(slideView);
        return slideView;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return representatives.size();
    }

    @Override
    public float getPageWidth(int position) {
        return 0.8f;
    }
}
