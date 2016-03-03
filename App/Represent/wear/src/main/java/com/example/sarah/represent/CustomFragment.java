package com.example.sarah.represent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Sarah on 3/1/2016.
 */
public class CustomFragment extends Fragment {

    private int portrait;
    private String type;
    private String name;
    private String party;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setArgs(int portrait, String type, String name, String party) {
        this.portrait = portrait;
        this.type = type;
        this.name = name;
        this.party = party;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);

//            ImageView portraitView = (ImageView) view.findViewById(R.id.fragment_portrait);
        ((ImageView) view.findViewById(R.id.fragment_portrait)).setImageResource(this.portrait);
//            portraitView.setImageResource(this.portrait);

//            TextView typeView = (TextView) view.findViewById(R.id.fragment_type);
        ((TextView) view.findViewById(R.id.fragment_type)).setText(this.type);
//            typeView.setText(this.type);

//            TextView nameView = (TextView) view.findViewById(R.id.fragment_name);
        ((TextView) view.findViewById(R.id.fragment_name)).setText(this.name);
//            nameView.setText(this.name);

//            TextView partyView = (TextView) view.findViewById(R.id.fragment_party_name);
        ((TextView) view.findViewById(R.id.fragment_party_name)).setText(this.party);
//            partyView.setText(this.party);

//            RelativeLayout partyColor = (RelativeLayout) view.findViewById(R.id.fragment_party_color);
        view.findViewById(R.id.fragment_party_color).setBackgroundResource(R.drawable.blue_circle);
//            partyColor.setBackgroundResource(R.drawable.blue_circle);

        return view;
    }

    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_layout, null);

//            ImageView portraitView = (ImageView) view.findViewById(R.id.fragment_portrait);
            ((ImageView) view.findViewById(R.id.fragment_portrait)).setImageResource(this.portrait);
//            portraitView.setImageResource(this.portrait);

//            TextView typeView = (TextView) view.findViewById(R.id.fragment_type);
            ((TextView) view.findViewById(R.id.fragment_type)).setText(this.type);
//            typeView.setText(this.type);

//            TextView nameView = (TextView) view.findViewById(R.id.fragment_name);
            ((TextView) view.findViewById(R.id.fragment_name)).setText(this.name);
//            nameView.setText(this.name);

//            TextView partyView = (TextView) view.findViewById(R.id.fragment_party_name);
            ((TextView) view.findViewById(R.id.fragment_party_name)).setText(this.party);
//            partyView.setText(this.party);

//            RelativeLayout partyColor = (RelativeLayout) view.findViewById(R.id.fragment_party_color);
            view.findViewById(R.id.fragment_party_color).setBackgroundResource(R.drawable.blue_circle);
//            partyColor.setBackgroundResource(R.drawable.blue_circle);

            return view;
    }

}
