package com.moling.micabrowser.datamigration;

import android.content.Context;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.micabrowser.MainActivity;
import com.moling.micabrowser.database.Entities.Bookmark;
import com.moling.micabrowser.database.Entities.History;
import com.moling.micabrowser.utils.ConfigUtils;
import com.moling.micabrowser.utils.DatabaseUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataMigration {
    public static void SettingsMigration(Context context) {
        String ExternalSearchEngine = ConfigUtils.getSearchEngine(context);
        switch (ExternalSearchEngine) {
            case "bing":
                ConfigUtils.setSearchEngine(context, "https://cn.bing.com/search?q=%search%");
            case "baidu":
                ConfigUtils.setSearchEngine(context, "https://www.baidu.com/s?wd=%search%");
        }
    }

    public static void BookmarksMigration(Context context) {
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + "mica_browser_data.json");
        String JsonStr = Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("bookmark");
        for (int i = JsonArr.size() - 1; i >= 0 ; i--) {
            DatabaseUtils.putBookmarkAsync(
                    MainActivity.bookmarkDAO,
                    new Bookmark(
                            JsonArr.getJSONObject(i).getString("url"),
                            JsonArr.getJSONObject(i).getString("title")
                    )
            );
        }
    }

    public static void HistoriesMigration(Context context) {
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + "mica_browser_data.json");
        String JsonStr = Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("history");;
        for (int i = JsonArr.size() - 1; i >= 0 ; i--) {
            DatabaseUtils.putHistoryAsync(
                    MainActivity.historyDAO,
                    new History(
                            JsonArr.getJSONObject(i).getString("url"),
                            JsonArr.getJSONObject(i).getString("title"),
                            System.currentTimeMillis()
                    )
            );
        }
    }

    public static String Read(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int result=0;
            StringBuilder sb = new StringBuilder();
            while((result=reader.read())!=-1) {
                sb.append((char)result);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {}
        return "";
    }
}
