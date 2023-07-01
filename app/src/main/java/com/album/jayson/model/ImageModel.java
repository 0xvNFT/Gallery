package com.album.jayson.model;

public class ImageModel {
    private final String path;
    private final String date;
    private boolean isVideo;

    public ImageModel(String path, String date) {
        this.path = path;
        this.date = date;
        this.isVideo = false;
    }

    public String getPath() {
        return path;
    }

    public String getDate() {
        return date;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
