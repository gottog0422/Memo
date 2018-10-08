package com.example.sh.memo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sh.memo.adapter.MemoAdapter;
import com.example.sh.memo.data.MemoData;
import com.example.sh.memo.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailCalActivity extends AppCompatActivity {
    @BindView(R.id.bt_back)
    ImageView bt_back;
    @BindView(R.id.bt_write)
    ImageView bt_write;
    @BindView(R.id.tv_cal)
    TextView tv_cal;
    @BindView(R.id.lv_cal_detail)
    ListView lv_cal_detail;
    @BindView(R.id.bt_cal_left)
    ImageView bt_cal_left;
    @BindView(R.id.bt_cal_right)
    ImageView bt_cal_right;


    Integer year;
    Integer day;
    Integer month;

    ArrayList<MemoData> items = new ArrayList<>();
    MemoAdapter memoAdapter;
    DBManager dbManager;

    Calendar calendar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cal);
        ButterKnife.bind(this);

        dbManager = new DBManager(DetailCalActivity.this, "mm.db", null, 1);

        Intent intent = getIntent();

        year = intent.getIntExtra("year", -1);
        month = intent.getIntExtra("month", -1);
        day = intent.getIntExtra("day", -1);

        calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, day);

        memoAdapter = new MemoAdapter(items, this);
        lv_cal_detail.setAdapter(memoAdapter);

        set_date();
    }

    public void set_date() {
        tv_cal.setText(year + "/" + (month + 1) + "/" + day);
        items.clear();
        items.addAll(dbManager.getYMD_List(year, month + 1, day));
        memoAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_cal)
    public void onclick_tv_cal() {
        DatePickerDialog dialog = new DatePickerDialog(DetailCalActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = m;
                day = d;
                calendar1.set(year, month, day);

                set_date();
            }
        }, year, month, day);

        dialog.show();
    }

    @OnClick(R.id.bt_cal_right)
    public void onclick_cal_right() {
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        year = calendar1.get(Calendar.YEAR);
        month = calendar1.get(Calendar.MONTH);
        day = calendar1.get(Calendar.DAY_OF_MONTH);

        set_date();
    }

    @OnClick(R.id.bt_cal_left)
    public void onclick_call_left() {
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        year = calendar1.get(Calendar.YEAR);
        month = calendar1.get(Calendar.MONTH);
        day = calendar1.get(Calendar.DAY_OF_MONTH);

        set_date();
    }

    @OnClick(R.id.bt_write)
    public void bt_write() {
        Intent intent = new Intent(DetailCalActivity.this, AddMemoActivity.class);
        intent.putExtra("id", -100);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        startActivity(intent);
    }

    @OnClick(R.id.bt_back)
    public void bt_back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        set_date();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}
