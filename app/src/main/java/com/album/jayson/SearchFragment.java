package com.album.jayson;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private ImageAdapter imageAdapter;
    private List<MediaFile> mediaFiles;
    private List<MediaFile> filteredMediaFiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        GridView gridView = rootView.findViewById(R.id.search_content_grid_view);
        EditText searchEditText = rootView.findViewById(R.id.search_edit_text);

        mediaFiles = new ArrayList<>();
        filteredMediaFiles = new ArrayList<>();
        imageAdapter = new ImageAdapter(requireContext(), filteredMediaFiles);
        gridView.setAdapter(imageAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMediaFiles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            MediaFile media = filteredMediaFiles.get(position);
            String mediaPath = media.getFilePath();

            if (media.getMimeType().startsWith("video/")) {
                launchVideoPlayerActivity(mediaPath);
            } else {
                launchFullImageActivity(mediaPath);
            }
        });

        if (hasStoragePermission()) {
            loadMediaFiles();
        } else {
            requestStoragePermission();
        }

        return rootView;
    }

    private void launchVideoPlayerActivity(String videoPath) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("videoPath", videoPath);
        startActivity(intent);
    }

    private void launchFullImageActivity(String imagePath) {
        Intent intent = new Intent(getActivity(), FullImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        startActivity(intent);
    }

    private void loadMediaFiles() {
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.MIME_TYPE};
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + " IN (?, ?)";
        String[] selectionArgs = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = requireActivity().getContentResolver().query(
                MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            int filePathColumnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int mimeTypeColumnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);

            while (cursor.moveToNext()) {
                String filePath = cursor.getString(filePathColumnIndex);
                String mimeType = cursor.getString(mimeTypeColumnIndex);

                if (filePath != null && mimeType != null) {
                    mediaFiles.add(new MediaFile(filePath, mimeType));
                }
            }
            cursor.close();
        }
        filteredMediaFiles.addAll(mediaFiles);
        imageAdapter.notifyDataSetChanged();
    }

    private void filterMediaFiles(String query) {
        filteredMediaFiles.clear();

        for (MediaFile mediaFile : mediaFiles) {
            if (mediaFile.getFilePath().toLowerCase().contains(query.toLowerCase())) {
                filteredMediaFiles.add(mediaFile);
            }
        }

        imageAdapter.notifyDataSetChanged();
    }

    private boolean hasStoragePermission() {
        int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }

    private static class ImageAdapter extends BaseAdapter {
        private final Context context;
        private final List<MediaFile> mediaFiles;

        public ImageAdapter(Context context, List<MediaFile> mediaFiles) {
            this.context = context;
            this.mediaFiles = mediaFiles;
        }

        @Override
        public int getCount() {
            return mediaFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            MediaFile mediaFile = mediaFiles.get(position);

            if (mediaFile.getMimeType().startsWith("image/")) {
                Glide.with(context).load(mediaFile.getFilePath()).into(imageView);
            } else if (mediaFile.getMimeType().startsWith("video/")) {
                imageView.setImageResource(R.drawable.video_placeholder);
            }

            return imageView;
        }
    }

    private static class MediaFile {
        private final String filePath;
        private final String mimeType;

        public MediaFile(String filePath, String mimeType) {
            this.filePath = filePath;
            this.mimeType = mimeType;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getMimeType() {
            return mimeType;
        }
    }
}
