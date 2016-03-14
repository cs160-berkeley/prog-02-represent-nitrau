package com.example.sarah.represent;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.CardFragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Sarah on 3/1/2016.
 */
public class CustomFragment extends Fragment implements View.OnClickListener{

    private Bitmap bmp;
    private byte[] pic;
    private String imgURL;
    private String type;
    private String name;
    private String party;
    private String bioID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setArgs(String bioID, String imgURL, String type, String name, String party) {
        this.bioID = bioID;
        this.imgURL = imgURL;
        this.type = type;
        this.name = name;
        this.party = party;
    }

    public String getBioID() {
        return bioID;
    }

    public String getImgURL() {
        return imgURL;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_layout, null);

        FrameLayout layout = (FrameLayout) view.findViewById(R.id.fragment_frame);
        layout.setOnClickListener(this);

        ((ImageView) view.findViewById(R.id.fragment_portrait)).setImageBitmap(bmp);

        ((TextView) view.findViewById(R.id.fragment_type)).setText(this.type);

        ((TextView) view.findViewById(R.id.fragment_name)).setText(this.name);

        ((TextView) view.findViewById(R.id.fragment_party_name)).setText(this.party);

        RelativeLayout partyColor = (RelativeLayout) view.findViewById(R.id.fragment_party_color);
        view.findViewById(R.id.fragment_party_color).setBackgroundResource(R.drawable.blue_circle);
        if (party.equalsIgnoreCase("D")) {
            partyColor.setBackgroundResource(R.drawable.blue_circle);
        } else if (party.equalsIgnoreCase("R")) {
            partyColor.setBackgroundResource(R.drawable.red_circle);
        } else {
            partyColor.setBackgroundResource(R.drawable.grey_circle);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("WEAR:MAIN", "Selected " + bioID);
        Intent intent = new Intent(getActivity(), WatchToPhoneService.class);
        intent.putExtra("ACTION", "detail");
        intent.putExtra("REP", bioID);
        getActivity().startService(intent);
    }
}
