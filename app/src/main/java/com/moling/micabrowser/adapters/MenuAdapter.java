package com.moling.micabrowser.adapters;

import com.moling.micabrowser.data.models.DownloadModel;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.data.models.URLModel;

import java.util.List;

public class MenuAdapter {
    public static List<URLModel> HistoryAdapter() {
        return Global.data.getHistory();
    }

    public static List<URLModel> BookmarkAdapter() {
        return Global.data.getBookMark();
    }

    public static List<DownloadModel> DownloadAdapter() {
        return Global.data.getDownload();
    }
}