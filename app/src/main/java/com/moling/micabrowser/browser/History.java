package com.moling.micabrowser.browser;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.micabrowser.models.HistoryModel;
import com.moling.micabrowser.utils.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class History {
    private static File file;
    public History(File file) {
        this.file = file;
    }

    public static List<HistoryModel> get() {
        List<HistoryModel> history = new ArrayList<>();
        String HistoryJsonStr = Stream.Read(file);
        JSONArray HistoryJsonArr;
        if (HistoryJsonStr.equals("")) {
            HistoryJsonArr = new JSONArray();
        } else {
            HistoryJsonArr = JSON.parseArray(HistoryJsonStr);
        }
        for (int i = HistoryJsonArr.size() - 1; i >= 0 ; i--) {
            history.add(new HistoryModel(
                    HistoryJsonArr.getJSONObject(i).getString("url"),
                    HistoryJsonArr.getJSONObject(i).getString("title")
            ));
        }
        return history;
    }

    public static void put(String title, String url) {
        String HistoryJsonStr = Stream.Read(file);
        JSONArray HistoryJsonArr;
        if (HistoryJsonStr.equals("")) {
            HistoryJsonArr = new JSONArray();
        } else {
            HistoryJsonArr = JSON.parseArray(HistoryJsonStr);
        }
        JSONObject History = new JSONObject();
        History.put("title", title);
        History.put("url", url);
        if (HistoryJsonArr.size() > 0) {
            if (!Objects.equals(url, HistoryJsonArr.getJSONObject(HistoryJsonArr.size() - 1).getString("url"))) {
                HistoryJsonArr.add(History);
            }
        } else {
            HistoryJsonArr.add(History);
        }
        Stream.write(HistoryJsonArr.toString(), file);
    }
}
