package com.zip.zipandroid.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class LazyPagerAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> mList;

    public LazyPagerAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}

