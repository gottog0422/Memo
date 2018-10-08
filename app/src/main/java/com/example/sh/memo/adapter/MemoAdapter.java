package com.example.sh.memo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sh.memo.R;
import com.example.sh.memo.data.MemoData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoAdapter extends BaseAdapter {
    ArrayList<MemoData> item;
    Context context;

    public MemoAdapter(ArrayList<MemoData> item, Context context) {
        this.item = item;
        this.context = context;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_memo, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        MemoData item = (MemoData) getItem(i);

        String date = item.getY().toString().substring(2) + "." + item.getM() + "." + item.getD();

        String ap;
        if (item.getAp() == 0) {
            ap = context.getString(R.string.am);
        } else {
            ap = context.getString(R.string.pm);
        }

        int tm = item.getTm();
        int th = item.getTh();
        if (th == 0) {
            th = 12;
        }
        String s_th = String.format("%02d", th);
        String s_tm = String.format("%02d", tm);

        String time = ap + " " + s_th + ":" + s_tm;


        if (item.getTitle().equals(viewGroup.getResources().getString(R.string.no_title))) {
            holder.tv_title.setText(item.getContent());
        } else {
            holder.tv_title.setText(item.getTitle());
        }

        holder.tv_date.setText(date);
        holder.tv_time.setText(time);

        if (item.getStar() == 1) {
            holder.iv_star.setImageDrawable(viewGroup.getResources().getDrawable(R.drawable.ic_star));
        } else {
            holder.iv_star.setImageDrawable(null);
        }

        return view;
    }

    static class Holder {
        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.iv_star)
        ImageView iv_star;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
