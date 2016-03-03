package com.example.sarah.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Sarah on 2/29/2016.
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private final ArrayList<Fragment> fragments;


    public GridPagerAdapter(Context mContext, FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.mContext = mContext;
        this.fragments = fragments;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Fragment f = fragments.get(col);
        return f;
    }

//    @Override
//    public Drawable getBackgroundForPage(int row, int column) {
//        return ContextCompat.getDrawable(mContext, R.drawable.rp_ca_13_lee_barbara);
//    }

    @Override
    public int getColumnCount(int i) {
        return fragments.size();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

}
