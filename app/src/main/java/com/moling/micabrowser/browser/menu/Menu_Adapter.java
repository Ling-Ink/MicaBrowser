package com.moling.micabrowser.browser.menu;

import com.moling.micabrowser.browser.History;
import com.moling.micabrowser.models.HistoryModel;

import java.util.List;

public class Menu_Adapter {
    public static List<HistoryModel> HistoryAdapter() {
        return History.get();
    }
}
