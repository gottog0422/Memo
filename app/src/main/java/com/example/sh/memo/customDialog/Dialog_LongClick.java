package com.example.sh.memo.customDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import com.example.sh.memo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_LongClick extends Dialog {
    @BindView(R.id.rl_modify)
    RelativeLayout rl_modify;
    @BindView(R.id.rl_delete)
    RelativeLayout rl_delete;
    @BindView(R.id.rl_share)
    RelativeLayout rl_share;

    private Context context;

    public interface CallBack {
        void bt_modify();

        void bt_delete();

        void bt_share();
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    CallBack callBack;

    public Dialog_LongClick(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_longclick);
        ButterKnife.bind(this);

        this.context = context;
    }

    @OnClick(R.id.rl_modify)
    public void click_modify() {
        if (callBack != null) {
            callBack.bt_modify();
        }
    }

    @OnClick(R.id.rl_delete)
    public void click_delete() {
        if (callBack != null) {
            callBack.bt_delete();
        }
    }

    @OnClick(R.id.rl_share)
    public void click_share() {
        if (callBack != null) {
            callBack.bt_share();
        }
    }
}
