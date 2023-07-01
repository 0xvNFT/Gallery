package com.album.jayson.libraryfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.album.jayson.FullImageActivity;
import com.album.jayson.R;
import com.album.jayson.VideoPlayerActivity;
import com.album.jayson.adapter.ImageAdapter;
import com.album.jayson.model.ImageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaysFragment extends Fragment {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 123;
    private final ImageAdapter.OnImageClickListener imageClickListener = imagePath -> {
        if (isVideoFile(imagePath)) {
            Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
            intent.putExtra("videoPath", imagePath);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), FullImageActivity.class);
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
        }
    };
    private ImageAdapter imageAdapter;
    private List<ImageModel> imageList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_days, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_days);
        imageList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(getContext(), imageList, ImageAdapter.FragmentType.DAY);
        imageAdapter.setOnImageClickListener(imageClickListener);
        recyclerView.setAdapter(imageAdapter);

        if (checkReadExternalStoragePermission()) {
            imageList.addAll(getImageData());
            imageAdapter.notifyDataSetChanged();
        } else {
            requestReadExternalStoragePermission();
        }

        return view;
    }

    private boolean checkReadExternalStoragePermission() {
        Context context = getContext();
        if (context != null) {
            int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            return permission == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestReadExternalStoragePermission() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST
            );
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageList.addAll(getImageData());
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    private List<ImageModel> getImageData() {
        List<ImageModel> imageData = new ArrayList<>();

        Context context = getContext();
        if (context == null) {
            return imageData;
        }

        Uri mediaUri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DATE_TAKEN};
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + " IN (" +
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + "," +
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO + ")";
        String sortOrder = MediaStore.Files.FileColumns.DATE_TAKEN + " DESC";

        Cursor cursor = context.getContentResolver().query(mediaUri, projection, selection, null, sortOrder);
        if (cursor != null) {
            int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            int columnIndexDateTaken = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN);

            while (cursor.moveToNext()) {
                String filePath = cursor.getString(columnIndexData);
                long dateTaken = cursor.getLong(columnIndexDateTaken);
                String dateTakenString = getDateOnly(dateTaken);

                ImageModel image = new ImageModel(filePath, dateTakenString);
                imageData.add(image);
            }

            cursor.close();
        }

        return imageData;
    }

    private String getDateOnly(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private boolean isVideoFile(String filePath) {
        String extension = getFileExtension(filePath);
        return extension != null && extension.equalsIgnoreCase("mp4");
    }

    private String getFileExtension(String filePath) {
        String extension = null;
        int lastDotIndex = filePath.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < filePath.length() - 1) {
            extension = filePath.substring(lastDotIndex + 1).toLowerCase();
        }
        return extension;
    }
}
