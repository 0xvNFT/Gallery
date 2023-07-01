package com.album.jayson;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.album.jayson.adapter.AlbumAdapter;
import com.album.jayson.model.AlbumModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlbumFragment extends Fragment {

    public AlbumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.album_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        List<AlbumModel> albumList = getAlbums();
        AlbumAdapter albumAdapter = new AlbumAdapter(requireContext(), albumList);
        recyclerView.setAdapter(albumAdapter);

        return view;
    }

    private List<AlbumModel> getAlbums() {
        List<AlbumModel> albums = new ArrayList<>();

        String[] projection = {MediaStore.MediaColumns.DATA};
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        try (android.database.Cursor cursor = requireContext().getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                Set<String> addedDirectories = new HashSet<>();
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(projection[0].indexOf(MediaStore.MediaColumns.DATA));
                    File directory = new File(filePath).getParentFile();
                    if (directory != null) {
                        String directoryPath = directory.getAbsolutePath();
                        if (!addedDirectories.contains(directoryPath)) {
                            String directoryName = directory.getName();
                            String thumbnailPath = getThumbnailPath(directoryPath);
                            albums.add(new AlbumModel(directoryName, directoryPath, thumbnailPath));
                            addedDirectories.add(directoryPath);
                        }
                    }
                }
            }
        }

        return albums;
    }

    private String getThumbnailPath(String directoryPath) {
        File[] files = new File(directoryPath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String filePath = file.getAbsolutePath();
                    if (isImageFile(filePath) || isVideoFile(filePath)) {
                        return filePath;
                    }
                }
            }
        }
        return null;
    }

    private boolean isImageFile(String filePath) {
        String extension = getFileExtension(filePath);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
                || extension.equalsIgnoreCase("png");
    }

    private boolean isVideoFile(String filePath) {
        String extension = getFileExtension(filePath);
        return extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mkv")
                || extension.equalsIgnoreCase("avi");
    }

    private String getFileExtension(String filePath) {
        String extension = "";
        int dotIndex = filePath.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            extension = filePath.substring(dotIndex + 1).toLowerCase();
        }
        return extension;
    }
}
