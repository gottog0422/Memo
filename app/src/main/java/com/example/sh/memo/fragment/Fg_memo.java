package com.example.sh.memo.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sh.memo.AddMemoActivity;
import com.example.sh.memo.DetailMemoActivity;
import com.example.sh.memo.R;
import com.example.sh.memo.adapter.MemoAdapter;
import com.example.sh.memo.bus.BusProvider;
import com.example.sh.memo.customDialog.Dialog_LongClick;
import com.example.sh.memo.data.MemoData;
import com.example.sh.memo.db.DBManager;
import com.example.sh.memo.event.Event_getFg_N;
import com.example.sh.memo.event.Event_refresh;
import com.example.sh.memo.event.Event_searchMode;
import com.example.sh.memo.event.Event_setSort;
import com.example.sh.memo.utils.PreferenceUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Fg_memo extends Fragment {
    private static Fg_memo curr = null;

    public static Fg_memo getInstance() {
        if (curr == null) {
            curr = new Fg_memo();
        }

        return curr;
    }

    @BindView(R.id.lv_memo)
    ListView lv_memo;

    ArrayList<MemoData> items = new ArrayList<>();
    MemoAdapter memoAdapter;
    DBManager dbManager;
    Bus bus = BusProvider.getInstance().getBus();

    Integer sort_type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_memo, container, false);
        ButterKnife.bind(this, view);
        bus.register(this);

        sort_type = PreferenceUtil.getInstance(getContext()).getIntegerExtra("value");

        dbManager = new DBManager(getActivity(), "mm.db", null, 1);

        memoAdapter = new MemoAdapter(items, container.getContext());
        lv_memo.setAdapter(memoAdapter);

        return view;
    }

    public void refresh() {
        items.clear();
        items.addAll(dbManager.getMemoList(sort_type));

        memoAdapter.notifyDataSetChanged();

        Event_refresh event = new Event_refresh();
        bus.post(event);
    }


    @OnItemClick(R.id.lv_memo)
    public void item_click(AdapterView<?> parent, int i) {
        MemoData item = items.get(i);
        Intent intent = new Intent(getActivity(), DetailMemoActivity.class);
        Integer id = item.getId();

        intent.putExtra("id", id);

        startActivityForResult(intent, 0);
        getActivity().overridePendingTransition(R.anim.anim_right_in, R.anim.anim_not_move);
    }

    @OnItemLongClick(R.id.lv_memo)
    public boolean item_long_Click(AdapterView<?> parent, int i) {
        set_dialog(i);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        dbManager.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Subscribe
    public void searchmode(Event_searchMode event) {
        items.clear();
        items.addAll(dbManager.getMemoList_Filter(event.getStr()));
        memoAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void set_sort(Event_setSort event) {
        sort_type = PreferenceUtil.getInstance(getContext()).getIntegerExtra("value");
        refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Event_getFg_N event_getFg_n = new Event_getFg_N(1);
            bus.post(event_getFg_n);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void set_dialog(final int position) {
        DisplayMetrics dm = getContext().getApplicationContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels;

        final Dialog_LongClick dialog_longClick = new Dialog_LongClick(getContext());

        dialog_longClick.setCallBack(new Dialog_LongClick.CallBack() {
            @Override
            public void bt_modify() {
                dialog_longClick.dismiss();
                Intent intent = new Intent(getActivity(), AddMemoActivity.class);
                intent.putExtra("id", items.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void bt_delete() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("경고");
                alertDialog.setMessage("정말 삭제하시겠습니까?");
                alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbManager.delet_Memo(items.get(position).getId());
                        refresh();
                    }
                });
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog_longClick.dismiss();
                alertDialog.show();
            }

            @Override
            public void bt_share() {
                String str = items.get(position).getTitle() + "\n" + items.get(position).getContent();
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("memo", str); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getContext(), getText(R.string.suc_copy), Toast.LENGTH_SHORT).show();

                dialog_longClick.dismiss();
            }
        });

        WindowManager.LayoutParams wm = dialog_longClick.getWindow().getAttributes();
        wm.copyFrom(dialog_longClick.getWindow().getAttributes());
        wm.width = w - (w / 4);

        dialog_longClick.show();
    }


}