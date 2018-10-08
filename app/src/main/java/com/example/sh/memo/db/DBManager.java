package com.example.sh.memo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sh.memo.data.MemoData;
import com.example.sh.memo.data.Setting;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE memo(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,content TEXT," +
                "year INTEGR,month INTEGER,day INTEGER,th INTEGER,tm INTEGER,ap INTEGER,star INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public ArrayList<MemoData> getMemoList(Integer type) {
        ArrayList<MemoData> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query;
        if (type == 1) {
            query = "select *from memo ORDER BY star DESC,year ASC,month ASC ,day ASC,id ASC;";
        } else if (type == 2) {
            query = "select *from memo ORDER BY star DESC,year DESC,month DESC ,day DESC,id DESC;";
        } else if (type == 3) {
            query = "select *from memo ORDER BY star DESC,title DESC;";
        } else {
            query = "select *from memo ORDER BY star DESC,title ASC;";
        }
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            Integer y = cursor.getInt(3);
            Integer m = cursor.getInt(4);
            Integer d = cursor.getInt(5);
            Integer th = cursor.getInt(6);
            Integer tm = cursor.getInt(7);
            Integer ap = cursor.getInt(8);
            Integer star = cursor.getInt(9);

            MemoData item = new MemoData(id, title, content, y, m, d, th, tm, ap, star);
            items.add(item);
        }
        cursor.close();
        return items;
    }

    public ArrayList<MemoData> getYMD_List(Integer year, Integer month, Integer day) {
        ArrayList<MemoData> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from memo where year = " + year + " and month = " + month + " and day = " + day + " ORDER BY star DESC,id DESC;";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            Integer y = cursor.getInt(3);
            Integer m = cursor.getInt(4);
            Integer d = cursor.getInt(5);
            Integer th = cursor.getInt(6);
            Integer tm = cursor.getInt(7);
            Integer ap = cursor.getInt(8);
            Integer star = cursor.getInt(9);

            MemoData item = new MemoData(id, title, content, y, m, d, th, tm, ap, star);
            items.add(item);
        }
        cursor.close();
        return items;
    }

    public ArrayList<MemoData> getMemoList_Filter(String search) {
        ArrayList<MemoData> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select *from memo where title like '%" + search + "%' or content like '%" + search + "%' ORDER BY star DESC,id DESC;";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            Integer y = cursor.getInt(3);
            Integer m = cursor.getInt(4);
            Integer d = cursor.getInt(5);
            Integer th = cursor.getInt(6);
            Integer tm = cursor.getInt(7);
            Integer ap = cursor.getInt(8);
            Integer star = cursor.getInt(9);

            MemoData item = new MemoData(id, title, content, y, m, d, th, tm, ap, star);
            items.add(item);
        }
        cursor.close();
        return items;
    }


    public void insertMemo(String title, String content, Integer y, Integer m, Integer d, Integer th, Integer tm, Integer ap, Integer star) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "Insert into memo values (null,'" + title + "','" + content +
                "','" + y + "','" + m + "','" + d + "','" + th + "','" + tm + "','" + ap + "','" + star + "')";
        db.execSQL(query);
    }

    public MemoData getMemoData(Integer memo_id) {
        MemoData item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from memo where id=" + memo_id + ";";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            Integer id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            Integer y = cursor.getInt(3);
            Integer m = cursor.getInt(4);
            Integer d = cursor.getInt(5);
            Integer th = cursor.getInt(6);
            Integer tm = cursor.getInt(7);
            Integer ap = cursor.getInt(8);
            Integer star = cursor.getInt(9);

            item = new MemoData(id, title, content, y, m, d, th, tm, ap, star);
        }
        cursor.close();

        return item;
    }

    public void change_MemoStar(Integer id, Integer star) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "";

        if (star == 0) {
            query = "UPDATE memo SET star = 0 WHERE id=" + id + ";";
        } else if (star == 1) {
            query = "UPDATE memo SET star = 1 WHERE id=" + id + ";";
        }
        db.execSQL(query);
    }

    public void modify_Memo(Integer id, String title, String content, Integer star) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "UPDATE memo SET title ='" + title + "',content ='" + content + "',star =" + star + " WHERE id=" + id + ";";

        db.execSQL(query);
    }

    public void delet_Memo(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "delete from memo where id=" + id + ";";

        db.execSQL(query);
    }

    public boolean check_cal(Integer y, Integer m, Integer d) {
        boolean t_f = false;

        SQLiteDatabase db = getReadableDatabase();
        String query = "select COUNT(*) from memo where year = " + y + " and month = " + m + " and day = " + d + ";";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            Integer count = cursor.getInt(0);

            if (count > 0) {
                t_f = true;
            } else {
                t_f = false;
            }
        }
        cursor.close();

        return t_f;
    }
}
