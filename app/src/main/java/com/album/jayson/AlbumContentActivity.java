package com.album.jayson;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.album.jayson.adapter.AlbumContentAdapter;
import com.album.jayson.model.AlbumContentModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumContentActivity extends AppCompatActivity {

    private List<AlbumContentModel> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_content);

        GridView gridView = findViewById(R.id.album_content_grid_view);

        String directoryPath = getIntent().getStringExtra("directoryPath");

        videoList = getVideosFromDirectory(directoryPath);

        AlbumContentAdapter albumContentAdapter = new AlbumContentAdapter(this, videoList);
        gridView.setAdapter(albumContentAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            AlbumContentModel albumContentModel = videoList.get(position);
            String videoPath = albumContentModel.getFilePath();

            Intent intent = new Intent(AlbumContentActivity.this, VideoPlayerActivity.class);
            intent.putExtra("videoPath", videoPath);
            startActivity(intent);
        });
    }

    private List<AlbumContentModel> getVideosFromDirectory(String directoryPath) {
        List<AlbumContentModel> videos = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isVideoFile(file.getName())) {
                    String filePath = file.getAbsolutePath();
                    String fileName = file.getName();
                    videos.add(new AlbumContentModel(fileName, filePath));
                }
            }
        }

        return videos;
    }

    private boolean isVideoFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("3gp");
    }
}
