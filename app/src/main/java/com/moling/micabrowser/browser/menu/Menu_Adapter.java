package com.moling.micabrowser.browser.menu;

import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.widgets.URL.URLModel;

import java.util.List;

public class Menu_Adapter {
    public static List<URLModel> HistoryAdapter() {
        return Global.history.get();
    }

    public static List<URLModel> BookmarkAdapter() {
        return Global.bookmark.get();
    }
}
