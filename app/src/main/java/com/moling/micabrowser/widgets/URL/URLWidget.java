package com.moling.micabrowser.widgets.URL;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.micabrowser.utils.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class URLWidget {
    private File file;
    public URLWidget(File file) {
        this.file = file;
    }

    public List<URLModel> get() {
        List<URLModel> history = new ArrayList<>();
        String HistoryJsonStr = Stream.Read(file);
        JSONArray HistoryJsonArr;
        if (HistoryJsonStr.equals("")) {
            HistoryJsonArr = new JSONArray();
        } else {
            HistoryJsonArr = JSON.parseArray(HistoryJsonStr);
        }
        for (int i = HistoryJsonArr.size() - 1; i >= 0 ; i--) {
            history.add(new URLModel(
                    HistoryJsonArr.getJSONObject(i).getString("url"),
                    HistoryJsonArr.getJSONObject(i).getString("title")
            ));
        }
        return history;
    }

    public void put(String title, String url) {
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
            } else if (!Objects.equals(title, HistoryJsonArr.getJSONObject(HistoryJsonArr.size() - 1).getString("title"))) {
                HistoryJsonArr.remove(HistoryJsonArr.size() - 1);
                HistoryJsonArr.add(History);
            }
        } else {
            HistoryJsonArr.add(History);
        }
        Stream.write(HistoryJsonArr.toString(), file);
    }

    public void delete(int position) {
        String HistoryJsonStr = Stream.Read(file);
        JSONArray HistoryJsonArr = JSON.parseArray(HistoryJsonStr);
        HistoryJsonArr.remove(HistoryJsonArr.size() - 1 - position);
        Stream.write(HistoryJsonArr.toString(), file);
    }
}
