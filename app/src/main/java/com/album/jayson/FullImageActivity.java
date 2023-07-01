package com.album.jayson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.album.jayson.model.ImageModel;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FullImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String STATE_IS_FAVORITE = "isFavorite";
    private static final String PREFS_NAME = "FavoritePrefs";
    private ImageView addFavorite;
    private String imagePath;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        PhotoView photoView = findViewById(R.id.photo_view);
        new PhotoViewAttacher(photoView);

        addFavorite = findViewById(R.id.add_favorite);
        addFavorite.setOnClickListener(this);

        imagePath = getIntent().getStringExtra("imagePath");
        fetchImageDate(imagePath);
        Glide.with(this)
                .load(imagePath)
                .into(photoView);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        isFavorite = sharedPreferences.getBoolean(imagePath, false);
        setFavoriteButtonState();

        if (savedInstanceState != null) {
            isFavorite = savedInstanceState.getBoolean(STATE_IS_FAVORITE);
            updateFavoriteButton();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_favorite) {
            if (isFavorite) {
                removeImageFromFavorites();
            } else {
                addImageToFavorites();
            }
        }
    }

    private void addImageToFavorites() {
        String date = fetchImageDate(imagePath);
        ImageModel image = new ImageModel(imagePath, date);

        FavoritesManager favoritesManager = new FavoritesManager(this);
        favoritesManager.addFavorite(image);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("isAdded", true);
        setResult(AppCompatActivity.RESULT_OK, resultIntent);
        finish();

        isFavorite = true;
        setFavoriteButtonState();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(imagePath, isFavorite);
        editor.apply();

        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
    }

    private void removeImageFromFavorites() {
        String date = fetchImageDate(imagePath);
        ImageModel image = new ImageModel(imagePath, date);

        FavoritesManager favoritesManager = new FavoritesManager(this);
        List<ImageModel> favorites = favoritesManager.getFavorites();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("isRemoved", true);
        setResult(AppCompatActivity.RESULT_OK, resultIntent);
        finish();

        if (favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {
                ImageModel favoriteImage = favorites.get(i);
                if (favoriteImage.getPath().equals(image.getPath())) {
                    favorites.remove(i);
                    break;
                }
            }
            favoritesManager.saveFavorites(favorites);
        }
        isFavorite = false;
        setFavoriteButtonState();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(imagePath, isFavorite);
        editor.apply();

        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_FAVORITE, isFavorite);
    }

    private void setFavoriteButtonState() {
        if (isFavorite) {
            addFavorite.setImageResource(R.drawable.favorite_active);
        } else {
            addFavorite.setImageResource(R.drawable.favorite_inactive);
        }
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            addFavorite.setImageResource(R.drawable.favorite_active);
        } else {
            addFavorite.setImageResource(R.drawable.favorite_inactive);
        }
    }

    private String fetchImageDate(String imagePath) {
        File imageFile = new File(imagePath);
        long lastModifiedTimestamp = imageFile.lastModified();
        Date lastModifiedDate = new Date(lastModifiedTimestamp);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(lastModifiedDate);
    }

}
