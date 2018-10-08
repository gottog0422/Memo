package com.example.sh.memo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public static final String PREFERENCE_NAME = "sort";
    private static PreferenceUtil preferencemodule = null;
    private static Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;


    public static PreferenceUtil getInstance(Context context) {
        mContext = context;

        if (preferencemodule == null) {
            preferencemodule = new PreferenceUtil();
        }
        if (prefs == null) {
            prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            editor = prefs.edit();
        }

        return preferencemodule;
    }

    public void putIntegerExtra(String key, Integer value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public Integer getIntegerExtra(String key) {
        return prefs.getInt(key, 1);
    }

    public void removePreference(String key) {
        editor.remove(key).commit();
    }
}
