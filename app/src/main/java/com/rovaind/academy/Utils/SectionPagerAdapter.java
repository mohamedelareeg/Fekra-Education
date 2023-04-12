package com.rovaind.academy.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed El Sayed
 */
/*
    Class that stores fragments for tabs
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SctionPagerAdapter";

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private  final List<Fragment> mFragmentList = new ArrayList<>(); // hold the Fragments
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position); //
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void AddFragment(Fragment fragment)
    {
        mFragmentList.add(fragment);//The list if private // need Somthing to Assign the List
    }
}
