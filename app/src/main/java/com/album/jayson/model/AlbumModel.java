package com.album.jayson.model;

public class AlbumModel {
    private final String directoryName;
    private final String directoryPath;
    private final String thumbnailPath;

    public AlbumModel(String directoryName, String directoryPath, String thumbnailPath) {
        this.directoryName = directoryName;
        this.directoryPath = directoryPath;
        this.thumbnailPath = thumbnailPath;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }
}
