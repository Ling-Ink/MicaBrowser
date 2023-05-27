package com.moling.micabrowser.data.adapters;

import com.moling.micabrowser.data.models.DownloadModel;
import com.moling.micabrowser.services.DownloadService;
import com.moling.micabrowser.ui.MainActivity;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.data.models.URLModel;

import java.util.List;
import java.util.Objects;

public class MenuAdapter {
    public static List<URLModel> HistoryAdapter() {
        return Global.data.getHistory();
    }

    public static List<URLModel> BookmarkAdapter() {
        return Global.data.getBookMark();
    }

    public static List<DownloadModel> DownloadAdapter() {
        List<DownloadModel> model = Global.data.getDownload();
        for (int i = 0; i < MainActivity.downloadProgress.size(); i++) {
            String hash = (String) MainActivity.downloadProgress.keySet().toArray()[i];
            for (int j = 0; j < model.size(); j++) {
                if (Objects.equals(model.get(j).getHash(), hash)) {
                    model.get(j).setProgress(MainActivity.downloadProgress.get(hash));
                }
            }
        }
        return model;
    }
}
