package com.moling.micabrowser.models;

public class HistoryModel {
    private String title;
    private String url;
    public HistoryModel(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
