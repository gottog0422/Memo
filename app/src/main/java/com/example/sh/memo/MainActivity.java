package com.example.sh.memo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sh.memo.adapter.VpAdapter;
import com.example.sh.memo.bus.BusProvider;
import com.example.sh.memo.customDialog.Dialog_sort;
import com.example.sh.memo.event.Event_getFg_N;
import com.example.sh.memo.event.Event_searchMode;
import com.example.sh.memo.event.Event_setSort;
import com.example.sh.memo.utils.PreferenceUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.bt_sort)
    ImageView bt_sort;
    @BindView(R.id.bt_search)
    ImageView bt_search;
    @BindView(R.id.bt_write)
    ImageView bt_write;
    @BindView(R.id.search_mode_back)
    ImageView bt_search_back;
    @BindView(R.id.search_mode_clear)
    ImageView bt_search_clear;
    @BindView(R.id.search_mode_et)
    EditText et_search;

    Bus bus = BusProvider.getInstance().getBus();
    VpAdapter vpAdapter;

    boolean search_mode = false;
    Integer fg_n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bus.register(this);

        et_search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        vpAdapter = new VpAdapter(getSupportFragmentManager(), this);
        vp.setAdapter(vpAdapter);

        tabs.setupWithViewPager(vp);
    }


    @OnClick(R.id.bt_search)
    public void search_mode() {
        search_mode = true;
        set_searchView();
        et_search.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @OnClick(R.id.search_mode_back)
    public void search_finish() {
        search_mode = false;

        search_clear();
        set_finish_searchView();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    @OnClick(R.id.search_mode_clear)
    public void search_clear() {
        onTextChanged("");
        et_search.setText("");
    }

    @OnClick(R.id.bt_write)
    public void write_memo() {
        Intent intent = new Intent(MainActivity.this, AddMemoActivity.class);
        intent.putExtra("id", -1);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick(R.id.bt_sort)
    public void click_sort() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels;

        final Dialog_sort dialog_sort = new Dialog_sort(this);

        dialog_sort.setCallBack(new Dialog_sort.CallBack() {
            @Override
            public void bt_sort1() {
                PreferenceUtil.getInstance(MainActivity.this).putIntegerExtra("value", 1);
                Event_setSort event = new Event_setSort();
                bus.post(event);
                dialog_sort.dismiss();
            }

            @Override
            public void bt_sort2() {
                PreferenceUtil.getInstance(MainActivity.this).putIntegerExtra("value", 2);
                Event_setSort event = new Event_setSort();
                bus.post(event);
                dialog_sort.dismiss();
            }

            @Override
            public void bt_sort3() {
                PreferenceUtil.getInstance(MainActivity.this).putIntegerExtra("value", 3);
                Event_setSort event = new Event_setSort();
                bus.post(event);
                dialog_sort.dismiss();
            }

            @Override
            public void bt_sort4() {
                PreferenceUtil.getInstance(MainActivity.this).putIntegerExtra("value", 4);
                Event_setSort event = new Event_setSort();
                bus.post(event);
                dialog_sort.dismiss();
            }
        });

        WindowManager.LayoutParams wm = dialog_sort.getWindow().getAttributes();
        wm.copyFrom(dialog_sort.getWindow().getAttributes());
        wm.width = w - (w / 4);

        dialog_sort.show();
    }

    @OnTextChanged(R.id.search_mode_et)
    protected void onTextChanged(CharSequence text) {
        String filter_str = text.toString();
        Event_searchMode event = new Event_searchMode(filter_str);
        bus.post(event);
    }

    @Override
    public void onBackPressed() {
        if (search_mode) {
            search_finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    @Subscribe
    public void event_getFg_N(Event_getFg_N event) {
        fg_n = event.getN();
        set_search();

        if (fg_n == 0 && search_mode) {
            search_finish();
        }
    }

    public void set_search() {
        if (fg_n == 0) {
            bt_search.setVisibility(View.GONE);
            bt_sort.setVisibility(View.GONE);
        } else if (fg_n == 1) {
            bt_search.setVisibility(View.VISIBLE);
            bt_sort.setVisibility(View.VISIBLE);
        }
    }

    public void set_searchView() {
        bt_write.setVisibility(View.GONE);
        bt_search.setVisibility(View.GONE);
        bt_sort.setVisibility(View.GONE);

        bt_search_back.setVisibility(View.VISIBLE);
        bt_search_clear.setVisibility(View.VISIBLE);
        et_search.setVisibility(View.VISIBLE);
    }

    public void set_finish_searchView() {
        bt_write.setVisibility(View.VISIBLE);
        bt_search.setVisibility(View.VISIBLE);
        bt_sort.setVisibility(View.VISIBLE);

        bt_search_back.setVisibility(View.GONE);
        bt_search_clear.setVisibility(View.GONE);
        et_search.setVisibility(View.GONE);
    }
}