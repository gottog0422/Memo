package com.example.sh.memo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sh.memo.R;
import com.example.sh.memo.fragment.Fg_calendar;
import com.example.sh.memo.fragment.Fg_memo;

public class VpAdapter extends FragmentStatePagerAdapter {
    Context context;

    public VpAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return Fg_calendar.getInstance();
        } else {
            return Fg_memo.getInstance();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.calendar_tab);
        } else {
            return context.getString(R.string.memo_tab);
        }
    }
}
