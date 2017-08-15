package com.tzw.noah.ui.circle;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> listFragments;
    List<String> mTitles;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> al, List<String> titles) {
        super(fm);
        mTitles = titles;
        listFragments = al;
    }

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> al) {
        super(fm);
        listFragments = al;
    }

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null)
            return null;
        if (position >= 0 && position < getCount()) {
            return mTitles.get(position);
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}