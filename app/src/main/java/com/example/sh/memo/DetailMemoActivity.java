package com.example.sh.memo;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sh.memo.bus.BusProvider;
import com.example.sh.memo.db.DBManager;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailMemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_memo);
        ButterKnife.bind(this);

        dbManager = new DBManager(DetailMemoActivity.this, "mm.db", null, 1);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
    }

    @OnClick(R.id.bt_detail_del)
    public void del() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("경고");
        alertDialog.setMessage("정말 삭제하시겠습니까?");
        alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbManager.delet_Memo(id);
                finish();
            }
        });
        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.show();
    }

    @OnClick(R.id.bt_detail_modify)
    public void modify() {
        Intent intent = new Intent(DetailMemoActivity.this, AddMemoActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);

        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnClick(R.id.bt_detail_star)
    public void star() {
        if (star == 0) {
            bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            star = 1;
        } else if (star == 1) {
            bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_star));
            star = 0;
        }
        dbManager.change_MemoStar(id, star);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();

        title = dbManager.getMemoData(id).getTitle();
        content = dbManager.getMemoData(id).getContent();
        star = dbManager.getMemoData(id).getStar();

        tv_title.setText(title);
        tv_content.setText(content);

        if (star == 0) {
            bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_star));
        } else if (star == 1) {
            bt_star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        dbManager.close();
    }

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.bt_detail_del)
    ImageView bt_del;
    @BindView(R.id.bt_detail_modify)
    ImageView bt_modify;
    @BindView(R.id.bt_detail_star)
    ImageView bt_star;

    Integer id;
    Integer star;
    String title;
    String content;

    Bus bus = BusProvider.getInstance().getBus();
    DBManager dbManager;

}
