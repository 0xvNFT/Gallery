package com.album.jayson;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.album.jayson.model.ImageModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    public static final String PREFS_NAME = "FavoritesPrefs";
    private static final String KEY_FAVORITES = "favorites";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public FavoritesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addFavorite(ImageModel image) {
        List<ImageModel> favorites = getFavorites();
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        favorites.add(image);
        saveFavorites(favorites);
    }

    public void removeFavorite(ImageModel image) {
        List<ImageModel> favorites = getFavorites();
        if (favorites != null) {
            favorites.remove(image);
            saveFavorites(favorites);
        }
    }

    public List<ImageModel> getFavorites() {
        String jsonFavorites = sharedPreferences.getString(KEY_FAVORITES, "");
        if (TextUtils.isEmpty(jsonFavorites)) {
            return new ArrayList<>();
        } else {
            Type type = new TypeToken<List<ImageModel>>() {
            }.getType();
            return gson.fromJson(jsonFavorites, type);
        }
    }

    void saveFavorites(List<ImageModel> favorites) {
        String jsonFavorites = gson.toJson(favorites);
        sharedPreferences.edit().putString(KEY_FAVORITES, jsonFavorites).apply();
    }
}

