package com.example.sarah.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by Sarah on 2/29/2016.
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;


    public GridPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        return null;
    }

    @Override
    public int getColumnCount(int i) {
        return 1;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    private static class Page {

    }
}
