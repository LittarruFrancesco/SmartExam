package com.smart.coffee.smartexam;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExamFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENTS_COUNT = 2;

    public ExamFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new InfoExamFragment();
            case 1:
                return new SessionListExamFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Info";
            case 1:
                return "Sessioni";
            default:
                return null;
        }
    }
}
