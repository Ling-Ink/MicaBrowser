package com.moling.micabrowser.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloads")
public class Download {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "column_id")
    public int column_id;

    @ColumnInfo(name = "file_name")
    public String file_name;

    @ColumnInfo(name = "file_loc")
    public String file_loc;

    @ColumnInfo(name = "down_url")
    public String down_url;

    @ColumnInfo(name = "down_progress")
    public int down_progress;

    @ColumnInfo(name = "timeStamp")
    public long timeStamp;

    public Download(String file_name, String file_loc, String down_url, int down_progress, long timeStamp) {
        this.file_name = file_name;
        this.file_loc = file_loc;
        this.down_url = down_url;
        this.down_progress = down_progress;
        this.timeStamp = timeStamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "downloads{" +
                "file_name='" + file_name + '\'' +
                ", file_loc='" + file_loc + '\'' +
                ", down_url='" + down_url + '\'' +
                ", down_progress='" + down_progress + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                "}";
    }
}
