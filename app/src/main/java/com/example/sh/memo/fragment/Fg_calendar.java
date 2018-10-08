package com.example.sh.memo.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sh.memo.DetailCalActivity;
import com.example.sh.memo.R;
import com.example.sh.memo.adapter.CalendarAdapter;
import com.example.sh.memo.bus.BusProvider;
import com.example.sh.memo.customDialog.Dialog_ym;
import com.example.sh.memo.db.DBManager;
import com.example.sh.memo.event.Event_getFg_N;
import com.example.sh.memo.event.Event_refresh;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class Fg_calendar extends Fragment {
    private static Fg_calendar curr = null;

    public static Fg_calendar getInstance() {
        if (curr == null) {
            curr = new Fg_calendar();
        }
        return curr;
    }

    @BindView(R.id.bt_cal_right)
    ImageView bt_right;
    @BindView(R.id.bt_cal_left)
    ImageView bt_left;
    @BindView(R.id.tv_cal_cal)
    TextView tv_cal;
    @BindView(R.id.gv_cal)
    GridView gv_cal;

    ArrayList<Integer> items = new ArrayList<>();

    CalendarAdapter calendarAdapter;
    DBManager dbManager;
    Bus bus = BusProvider.getInstance().getBus();

    Integer year;
    Integer month;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_calendar, container, false);
        ButterKnife.bind(this, view);
        bus.register(this);

        dbManager = new DBManager(getActivity(), "mm.db", null, 1);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);

        refreshCalendar(year, month);
        setDate(year, month);
        return view;
    }

    public void refreshCalendar(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int startDay = calendar.get(Calendar.DAY_OF_WEEK);
        int lastDay = calendar.getActualMaximum(Calendar.DATE);

        items.clear();

        for (int i = 0; i < startDay - 1; i++) {
            items.add(0);
        }

        for (int i = 1; i <= lastDay; i++) {
            items.add(i);
        }

        calendarAdapter = new CalendarAdapter(items, dbManager, year, month + 1, getContext());
        gv_cal.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
    }

    public void setDate(int year, int month) {
        tv_cal.setText(year + " / " + (month + 1));
    }

    @OnClick(R.id.bt_cal_left)
    public void onclick_bt_left() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.add(Calendar.MONTH, -1);

        year = calendar1.get(Calendar.YEAR);
        month = calendar1.get(Calendar.MONTH);

        refreshCalendar(year, month);
        setDate(year, month);
    }

    @OnClick(R.id.bt_cal_right)
    public void onclick_bt_right() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.add(Calendar.MONTH, 1);

        year = calendar1.get(Calendar.YEAR);
        month = calendar1.get(Calendar.MONTH);

        refreshCalendar(year, month);
        setDate(year, month);
    }


    @OnClick(R.id.tv_cal_cal)
    public void select_cal() {
        DisplayMetrics dm = getContext().getApplicationContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels;

        final Dialog_ym dialog_ym = new Dialog_ym(getContext(), year, month);

        dialog_ym.setCallBack(new Dialog_ym.CallBack() {
            @Override
            public void bt_cancle() {
                dialog_ym.dismiss();
            }

            @Override
            public void bt_ok(int y, int m) {
                year = y;
                month = m - 1;
                refreshCalendar(year, month);
                setDate(year, month);
                dialog_ym.dismiss();
            }
        });

        WindowManager.LayoutParams wm = dialog_ym.getWindow().getAttributes();
        wm.copyFrom(dialog_ym.getWindow().getAttributes());
        wm.width = w - (w / 4);

        dialog_ym.show();
    }


    @OnItemClick(R.id.gv_cal)
    public void onitemclick_gv(AdapterView<?> parent, int i) {
        Intent intent = new Intent(getActivity(), DetailCalActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", items.get(i));

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe
    public void refresh_event(Event_refresh event) {
        refreshCalendar(year, month);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Event_getFg_N event_getFg_n = new Event_getFg_N(0);
            bus.post(event_getFg_n);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
