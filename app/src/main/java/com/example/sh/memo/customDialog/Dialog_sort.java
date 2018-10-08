package com.example.sh.memo.customDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import com.example.sh.memo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_sort extends Dialog {
    @BindView(R.id.rl_1)
    RelativeLayout rl_1;
    @BindView(R.id.rl_2)
    RelativeLayout rl_2;
    @BindView(R.id.rl_3)
    RelativeLayout rl_3;
    @BindView(R.id.rl_4)
    RelativeLayout rl_4;

    Context context;

    public interface CallBack {
        void bt_sort1();

        void bt_sort2();

        void bt_sort3();

        void bt_sort4();
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    CallBack callBack;

    public Dialog_sort(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_sort);
        ButterKnife.bind(this);
        this.context = context;


    }

    @OnClick(R.id.rl_1)
    public void click_rl1() {
        if (callBack != null) {
            callBack.bt_sort1();
        }
    }

    @OnClick(R.id.rl_2)
    public void click_rl2() {
        if (callBack != null) {
            callBack.bt_sort2();
        }
    }

    @OnClick(R.id.rl_3)
    public void click_rl3() {
        if (callBack != null) {
            callBack.bt_sort3();
        }
    }

    @OnClick(R.id.rl_4)
    public void click_rl4() {
        if (callBack != null) {
            callBack.bt_sort4();
        }
    }
}
