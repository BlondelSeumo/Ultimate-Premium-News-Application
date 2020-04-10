package com.virmana.news_app.Provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.virmana.news_app.model.Language;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Tamim on 30/09/2017.
 */

public class LanguageStorage {
    private final String STORAGE = "MY_LANG";
    private SharedPreferences preferences;
    private Context context;
    public LanguageStorage(Context context) {
        this.context = context;
    }
    public void StoreLang(ArrayList<Language> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("LANG_LIST", json);
        editor.apply();
    }

    public ArrayList<Language> loadLang() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("LANG_LIST", null);
        Type type = new TypeToken<ArrayList<Language>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    public void ClearLang() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
