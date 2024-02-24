package com.moling.micabrowser.data.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "histories")
public class History {
    @PrimaryKey()
    @ColumnInfo(name = "page_url")
    @NonNull
    public String page_url;

    @ColumnInfo(name = "page_title")
    public String page_title;

    @ColumnInfo(name = "timeStamp")
    public long timeStamp;

    public History(@NonNull String page_url, String page_title, long timeStamp) {
        this.page_url = page_url;
        this.page_title = page_title;
        this.timeStamp = timeStamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "histories{" +
                "page_url='" + page_url + '\'' +
                ", page_title='" + page_title + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                "}";
    }

}
