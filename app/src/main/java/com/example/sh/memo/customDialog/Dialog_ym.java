package com.example.sh.memo.customDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.sh.memo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_ym extends Dialog {

    private Context context;

    @BindView(R.id.picker_year)
    NumberPicker picker_year;
    @BindView(R.id.picker_month)
    NumberPicker picker_month;
    @BindView(R.id.bt_cancel)
    Button bt_cancel;
    @BindView(R.id.bt_ok)
    Button bt_ok;

    public interface CallBack {
        void bt_cancle();

        void bt_ok(int y, int m);
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    CallBack callBack;

    public Dialog_ym(@NonNull Context context, int year, int month) {
        super(context);
        setContentView(R.layout.dialog_ym);
        ButterKnife.bind(this);

        picker_year.setMinValue(1980);
        picker_year.setMaxValue(2099);

        picker_month.setMinValue(1);
        picker_month.setMaxValue(12);

        picker_year.setValue(year);
        picker_month.setValue(month+1);
    }

    @OnClick(R.id.bt_ok)
    public void click_ok() {
        if (callBack != null) {
            callBack.bt_ok(picker_year.getValue(), picker_month.getValue());
        }
    }

    @OnClick(R.id.bt_cancel)
    public void click_cancel() {
        if (callBack != null) {
            callBack.bt_cancle();
        }
    }

}
