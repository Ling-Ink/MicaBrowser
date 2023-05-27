package com.moling.micabrowser.data.models;

public class DownloadModel {
    private String name;
    private String location;
    private String hash;
    private float progress;
    public DownloadModel(String name, String location, String hash, float progress) {
        this.name = name;
        this.location = location;
        this.hash = hash;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getHash() {
        return hash;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
