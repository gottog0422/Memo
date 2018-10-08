package com.example.sh.memo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sh.memo.R;
import com.example.sh.memo.db.DBManager;


import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarAdapter extends BaseAdapter {
    ArrayList<Integer> items;
    Context context;
    DBManager db;
    Integer year;
    Integer month;

    Integer m_m;
    Integer m_y;
    Integer m_d;

    public CalendarAdapter(ArrayList<Integer> items, DBManager db, Integer year, Integer month, Context context) {
        this.items = items;
        this.year = year;
        this.month = month;
        this.db = db;
        this.context = context;

        Calendar cal = Calendar.getInstance();
        m_m = cal.get(Calendar.MONTH) + 1;
        m_y = cal.get(Calendar.YEAR);
        m_d = cal.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cal, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Integer item = (Integer) getItem(i);

        if (item != 0) {
            holder.tv_day.setText(item.toString());

            if (db.check_cal(year, month, item)) {
                holder.iv_check.setVisibility(View.VISIBLE);
            }

            if ((i + 1) % 7 == 0) {
                holder.tv_day.setTextColor(Color.BLUE);
            } else if ((i + 1) % 7 == 1) {
                holder.tv_day.setTextColor(Color.RED);
            }

        } else {
            holder.tv_day.setText("");
        }

        if (m_y.equals(year) && m_m.equals(month) && m_d.equals(item)) {

            holder.rl.setBackground(context.getResources().getDrawable(R.drawable.tedori));
        }

        return view;
    }

    static class Holder {
        @BindView(R.id.tv_calitem_day)
        TextView tv_day;
        @BindView(R.id.iv_calitem_check)
        ImageView iv_check;
        @BindView(R.id.rl)
        RelativeLayout rl;


        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
