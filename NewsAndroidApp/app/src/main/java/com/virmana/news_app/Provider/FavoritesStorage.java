package com.virmana.news_app.Provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.virmana.news_app.model.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Tamim on 01/11/2017.
 */



public class FavoritesStorage {
    private final String STORAGE_VIDEO = "MY_FAVORITE_VIDEO";
    private final String STORAGE_IMAGE = "MY_FAVORITE_IMAGE";
    private final String STORAGE_GIF = "MY_FAVORITE_GIF";
    private final String STORAGE_JOKE = "MY_FAVORITE_JOKE";
    private final String STORAGE_STATUS = "MY_FAVORITE_STATUS";
    private final String STORAGE_APPS = "MY_APPS";
    private SharedPreferences preferences;
    private Context context;
    public FavoritesStorage(Context context) {
        this.context = context;
    }

    public void storeImage(ArrayList<Post> arrayList) {
        preferences = context.getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("favoritesListImage", json);
        editor.apply();
    }
    public ArrayList<Post> loadImagesFavorites() {
        preferences = context.getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("favoritesListImage", null);
        Type type = new TypeToken<ArrayList<Post>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}

