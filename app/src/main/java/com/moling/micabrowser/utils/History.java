package com.moling.micabrowser.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class History {
    private static File file;
    public History(File file) {
        this.file = file;
    }

    public static Map<String, String> get() {
        Map<String, String> history = new HashMap<>();
        String HistoryJsonStr = Stream.Read(file);
        JSONArray HistoryJsonArr;
        if (HistoryJsonStr.equals("")) {
            HistoryJsonArr = new JSONArray();
        } else {
            HistoryJsonArr = JSON.parseArray(HistoryJsonStr);
        }
        for (int i = 0; i < HistoryJsonArr.size(); i++) {
            history.put(HistoryJsonArr.getJSONObject(i).getString("title"), HistoryJsonArr.getJSONObject(i).getString("url"));
        }
        return history;
    }

    public static void append(String title, String url) {
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
