package com.moling.micabrowser.data.models;

public class DownloadModel {
    private String name;
    private String location;
    public DownloadModel(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
