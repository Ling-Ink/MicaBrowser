package com.moling.micabrowser.utils;

import com.moling.micabrowser.data.database.DAO.BookmarkDAO;
import com.moling.micabrowser.data.database.DAO.DownloadDAO;
import com.moling.micabrowser.data.database.DAO.HistoryDAO;
import com.moling.micabrowser.data.database.Entities.Bookmark;
import com.moling.micabrowser.data.database.Entities.Download;
import com.moling.micabrowser.data.database.Entities.History;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    private static List<Bookmark> getAllBookmarksAsync_Results;
    public static List<Bookmark> getAllBookmarksAsync(BookmarkDAO bookmarkDAO) {
        getAllBookmarksAsync_Results = new ArrayList<>();
        Thread thread = new Thread(() -> getAllBookmarksAsync_Results = bookmarkDAO.getAllBookmarks());
        thread.start();
        try {
            thread.join();
            return getAllBookmarksAsync_Results;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void putBookmarkAsync(BookmarkDAO bookmarkDAO, Bookmark bookmark) {
        new Thread(() -> bookmarkDAO.insertBookmark(bookmark)).start();
    }
    public static void delBookmarkAsync(BookmarkDAO bookmarkDAO, Bookmark bookmark) {
        new Thread(() -> bookmarkDAO.deleteBookmark(bookmark)).start();
    }

    private static List<Download> getAllDownloadsAsync_results;
    public static List<Download> getAllDownloadsAsync(DownloadDAO downloadDAO) {
        getAllDownloadsAsync_results = new ArrayList<>();
        Thread thread = new Thread(() -> getAllDownloadsAsync_results = downloadDAO.getAllDownloads());
        thread.start();
        try {
            thread.join();
            return getAllDownloadsAsync_results;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void putDownloadAsync(DownloadDAO downloadDAO, Download download) {
        new Thread(() -> downloadDAO.insertDownload(download)).start();
    }
    public static void delDownloadAsync(DownloadDAO downloadDAO, Download download) {
        new Thread(() -> downloadDAO.deleteDownload(download)).start();
    }

    private static List<History> getAllHistoriesAsync_results;
    public static List<History> getAllHistoriesAsync(HistoryDAO historyDAO) {
        getAllHistoriesAsync_results = new ArrayList<>();
        Thread thread = new Thread(() -> getAllHistoriesAsync_results = historyDAO.getAllHistories());
        thread.start();
        try {
            thread.join();
            return getAllHistoriesAsync_results;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void putHistoryAsync(HistoryDAO historyDAO, History history) {
        new Thread(() -> historyDAO.insertHistory(history)).start();
    }
    public static void delHistoryAsync(HistoryDAO historyDAO, History history) {
        new Thread(() -> historyDAO.deleteHistory(history));
    }
}
