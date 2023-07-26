package com.moling.micabrowser.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "column_id")
    public int column_id;

    @ColumnInfo(name = "page_url")
    public String page_url;

    @ColumnInfo(name = "page_title")
    public String page_title;

    public Bookmark(String page_url, String page_title) {
        this.page_url = page_url;
        this.page_title = page_title;
    }

    @NonNull
    @Override
    public String toString() {
        return "bookmarks{" +
                "page_url='" + page_url + '\'' +
                ", page_title='" + page_title + '\'' +
                "}";
    }
}
