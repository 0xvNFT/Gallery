package com.album.jayson;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.album.jayson.adapter.ImageAdapter;
import com.album.jayson.model.ImageModel;

import java.util.List;

public class FavoriteFragment extends Fragment implements ImageAdapter.OnImageClickListener {

    private ImageAdapter imageAdapter;
    private FavoritesManager favoritesManager;
    private TextView emptyTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritesManager = new FavoritesManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.favorite_recycler_view);
        emptyTextView = view.findViewById(R.id.empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<ImageModel> favoriteImages = favoritesManager.getFavorites();
        imageAdapter = new ImageAdapter(requireContext(), favoriteImages, ImageAdapter.FragmentType.FAVORITE);
        imageAdapter.setOnImageClickListener(this);
        recyclerView.setAdapter(imageAdapter);

        updateEmptyTextViewVisibility(favoriteImages);

        return view;
    }

    @Override
    public void onImageClick(String imagePath) {
        openFullImageActivity(imagePath);
    }

    private void openFullImageActivity(String imagePath) {
        Intent intent = new Intent(requireContext(), FullImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            boolean isRemoved = data.getBooleanExtra("isRemoved", false);
            if (isRemoved) {
                List<ImageModel> favoriteImages = favoritesManager.getFavorites();
                imageAdapter.updateData(favoriteImages);
            }
        }
    }

    private void updateEmptyTextViewVisibility(List<ImageModel> favoriteImages) {
        if (favoriteImages != null && !favoriteImages.isEmpty()) {
            emptyTextView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

}