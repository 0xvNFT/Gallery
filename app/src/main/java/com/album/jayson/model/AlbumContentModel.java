package com.album.jayson.model;

public class AlbumContentModel {
    private final String fileName;
    private final String filePath;

    public AlbumContentModel(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}

