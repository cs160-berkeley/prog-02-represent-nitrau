package com.example.sarah.represent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Sarah on 3/1/2016.
 */
public class VoteFragment extends Fragment {

    String location;
    int obama;
    int romney;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vote_layout, null);
        ((TextView) view.findViewById(R.id.vote_district)).setText(location);
        ((ProgressBar)view.findViewById(R.id.obama_bar)).setProgress(obama);
        ((TextView) view.findViewById(R.id.obamacent)).setText(obama + "%");
        ((ProgressBar) view.findViewById(R.id.romney_bar)).setProgress(romney);
        ((TextView) view.findViewById(R.id.romcent)).setText(romney + "%");
        return view;
    }

    public void setArgs(String location, int obama, int romney) {
        this.location = location;
        this.obama = obama;
        this.romney = romney;
    }
}
