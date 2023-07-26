package com.moling.micabrowser.database;

import androidx.room.RoomDatabase;

import com.moling.micabrowser.database.DAO.BookmarkDAO;
import com.moling.micabrowser.database.DAO.DownloadDAO;
import com.moling.micabrowser.database.DAO.HistoryDAO;
import com.moling.micabrowser.database.Entities.Bookmark;
import com.moling.micabrowser.database.Entities.Download;
import com.moling.micabrowser.database.Entities.History;

@androidx.room.Database(entities = {Bookmark.class, Download.class, History.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract BookmarkDAO bookmarkDAO();
    public abstract DownloadDAO downloadDAO();
    public abstract HistoryDAO historyDAO();
}
