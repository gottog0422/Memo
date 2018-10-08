package com.example.sh.memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sh.memo.bus.BusProvider;
import com.example.sh.memo.db.DBManager;
import com.squareup.otto.Bus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMemoActivity extends AppCompatActivity {
    @BindView(R.id.bt_addmemo_add)
    ImageView bt_add;
    @BindView(R.id.bt_addmemo_star)
    ImageView bt_star;
    @BindView(R.id.et_addmemo_content)
    EditText et_content;
    @BindView(R.id.et_addmemo_title)
    EditText et_title;

    Integer id;

    Integer mYear;
    Integer mMonth;
    Integer mDay;

    Integer mH;
    Integer mM;
    int ap;

    Integer star = 0;


    Boolean modify;

    DBManager dbManager;
    Bus bus = BusProvider.getInstance().getBus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        ButterKnife.bind(this);

        dbManager = new DBManager(AddMemoActivity.this, "mm.db", null, 1);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);


        if (id == -1) {
            //일반 add
        } else if (id == -100) {
            //날짜 정해서 add
            mYear = intent.getIntExtra("year", -1);
            mMonth=intent.getIntExtra("month",-1);
            mDay=intent.getIntExtra("day",-1);
        } else {
            //수정 add
            if (dbManager.getMemoData(id).getTitle().toString().equals(getResources().getString(R.string.no_title))) {
                et_title.setText("");
            } else {
                et_title.setText(dbManager.getMemoData(id).getTitle().toString());
            }

            et_content.setText(dbManager.getMemoData(id).getContent().toString());
            star = dbManager.getMemoData(id).getStar();

            if (star == 0) {
                bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_star));
            } else if (star == 1) {
                bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            }
        }

    }

    @OnClick(R.id.bt_addmemo_star)
    public void star() {
        if (star == 0) {
            bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            star = 1;
        } else if (star == 1) {
            bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_star));
            star = 0;
        }
    }

    @OnClick(R.id.bt_addmemo_add)
    public void add() {
        if (id == -1) {
            add_memo();
        } else if (id == -100) {
            add_cal_memo();
        } else {
            modify_Memo();
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (id == -1) {
            add_memo();
        } else if (id == -100) {
            add_cal_memo();
        } else {
            modify_Memo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
    }

    public void modify_Memo() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        if (content.replaceAll(" ", "").length() == 0) {
        } else {
            if (title.equals(dbManager.getMemoData(id).getTitle().toString())
                    && content.equals(dbManager.getMemoData(id).getContent().toString())) {
                if (star != dbManager.getMemoData(id).getStar()) {
                    dbManager.change_MemoStar(id, star);
                }
            } else {
                if (title.replaceAll(" ", "").length() == 0) {
                    title = getResources().getString(R.string.no_title);
                }

                dbManager.modify_Memo(id, title, content, star);
            }
        }
    }

    public void add_memo() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        if (content.replaceAll(" ", "").length() == 0) {
        } else {
            if (title.replaceAll(" ", "").length() == 0) {
                title = getResources().getString(R.string.no_title);
            }
            Calendar cal = Calendar.getInstance();

            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mH = cal.get(Calendar.HOUR);
            mM = cal.get(Calendar.MINUTE);
            ap = cal.get(Calendar.AM_PM);

            dbManager.insertMemo(title, content, mYear, mMonth + 1, mDay, mH, mM, ap, star);
        }
    }

    public void add_cal_memo() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        if (content.replaceAll(" ", "").length() == 0) {
        } else {
            if (title.replaceAll(" ", "").length() == 0) {
                title = getResources().getString(R.string.no_title);
            }
            Calendar cal = Calendar.getInstance();

            mH = cal.get(Calendar.HOUR);
            mM = cal.get(Calendar.MINUTE);
            ap = cal.get(Calendar.AM_PM);

            dbManager.insertMemo(title, content, mYear, mMonth + 1, mDay, mH, mM, ap, star);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        dbManager.close();
    }
}
