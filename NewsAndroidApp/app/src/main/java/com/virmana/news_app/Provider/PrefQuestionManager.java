package com.virmana.news_app.Provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by hsn on 01/01/2018.
 */

public class PrefQuestionManager {
    private final String STORAGE = "MY_FAVORITE";
    private SharedPreferences preferences;
    private Context context;
    public PrefQuestionManager(Context context) {
        this.context = context;
    }
    public void storeVoted(ArrayList<Integer> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("votedList", json);
        editor.apply();
    }

    public ArrayList<Integer> loadVoted() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("votedList", null);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
