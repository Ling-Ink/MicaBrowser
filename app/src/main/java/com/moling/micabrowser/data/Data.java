package com.moling.micabrowser.data;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.micabrowser.data.models.DownloadModel;
import com.moling.micabrowser.data.models.URLModel;
import com.moling.micabrowser.utils.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Data {
    private File file;

    public Data(String absolutePath) {
        this.file = new File(absolutePath + File.separator + "mica_browser_data.json");
        String DataStr = Stream.Read(file);
        if (DataStr.equals("")) {
            JSONObject JsonObj = new JSONObject();
            JsonObj.put("history", new JSONArray());
            JsonObj.put("bookmark", new JSONArray());
            JsonObj.put("downloads", new JSONArray());
            Stream.write(JsonObj.toString(), file);
        } else {
            JSONObject JsonObj = JSON.parseObject(DataStr);
            try { JSONArray JsonArr = JsonObj.getJSONArray("history");
            } catch (Exception e) { JsonObj.put("history", new JSONArray()); }
            try { JSONArray JsonArr = JsonObj.getJSONArray("bookmark");
            } catch (Exception e) { JsonObj.put("bookmark", new JSONArray()); }
            try { JSONArray JsonArr = JsonObj.getJSONArray("downloads");
            } catch (Exception e) { JsonObj.put("downloads", new JSONArray()); }
        }
    }

    public List<URLModel> getHistory() {
        List<URLModel> model = new ArrayList<>();
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("history");;
        for (int i = JsonArr.size() - 1; i >= 0 ; i--) {
            model.add(new URLModel(
                    JsonArr.getJSONObject(i).getString("url"),
                    JsonArr.getJSONObject(i).getString("title")
            ));
        }
        return model;
    }
    public void putHistory(String title, String url) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("history");
        JSONObject History = new JSONObject();
        History.put("title", title);
        History.put("url", url);
        if (JsonArr.size() > 0) {
            if (!Objects.equals(url, JsonArr.getJSONObject(JsonArr.size() - 1).getString("url"))) {
                JsonArr.add(History);
            } else if (!Objects.equals(title, JsonArr.getJSONObject(JsonArr.size() - 1).getString("title"))) {
                JsonArr.remove(JsonArr.size() - 1);
                JsonArr.add(History);
            }
        } else {
            JsonArr.add(History);
        }
        Stream.write(JsonObj.toString(), file);
    }
    public void delHistory(int position) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);;
        JSONArray JsonArr = JsonObj.getJSONArray("history");
        JsonArr.remove(JsonArr.size() - position - 1);
        Stream.write(JsonObj.toString(), file);
    }

    public List<URLModel> getBookMark() {
        List<URLModel> model = new ArrayList<>();
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("bookmark");
        for (int i = JsonArr.size() - 1; i >= 0 ; i--) {
            model.add(new URLModel(
                    JsonArr.getJSONObject(i).getString("url"),
                    JsonArr.getJSONObject(i).getString("title")
            ));
        }
        return model;
    }
    public void putBookMark(String title, String url) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("bookmark");
        JSONObject Bookmark = new JSONObject();
        Bookmark.put("title", title);
        Bookmark.put("url", url);
        if (JsonArr.size() > 0) {
            if (!Objects.equals(url, JsonArr.getJSONObject(JsonArr.size() - 1).getString("url"))) {
                JsonArr.add(Bookmark);
            } else if (!Objects.equals(title, JsonArr.getJSONObject(JsonArr.size() - 1).getString("title"))) {
                JsonArr.remove(JsonArr.size() - 1);
                JsonArr.add(Bookmark);
            }
        } else {
            JsonArr.add(Bookmark);
        }
        Stream.write(JsonObj.toString(), file);
    }
    public void delBookMark(int position) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);;
        JSONArray JsonArr = JsonObj.getJSONArray("bookmark");
        JsonArr.remove(JsonArr.size() - position - 1);
        Stream.write(JsonObj.toString(), file);
    }

    public List<DownloadModel> getDownload() {
        List<DownloadModel> model = new ArrayList<>();
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("downloads");
        for (int i = JsonArr.size() - 1; i >= 0 ; i--) {
            model.add(new DownloadModel(
                    JsonArr.getJSONObject(i).getString("name"),
                    JsonArr.getJSONObject(i).getString("location"),
                    // 旧版本兼容
                    JsonArr.getJSONObject(i).containsKey("hash") ? JsonArr.getJSONObject(i).getString("hash") : "",
                    JsonArr.getJSONObject(i).containsKey("progress") ? JsonArr.getJSONObject(i).getFloat("progress") : 0
            ));
        }
        return model;
    }
    public void putDownload(String name, String location, String hash, float progress) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("downloads");
        JSONObject Download = new JSONObject();
        Download.put("name", name);
        Download.put("location", location);
        Download.put("hash", hash);
        Download.put("progress", progress);
        JsonArr.add(Download);
        Stream.write(JsonObj.toString(), file);
    }
    public void progressDownload(String hash, float progress) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("downloads");
        for (int i = 0; i < JsonArr.size(); i++) {
            if (Objects.equals(JsonArr.getJSONObject(i).getString("hash"), hash)) {
                JsonArr.getJSONObject(i).replace("progress", progress);
                Stream.write(JsonObj.toString(), file);
            }
        }
    }
    public void delDownload(int position) {
        String JsonStr = Stream.Read(file);
        JSONObject JsonObj = JSON.parseObject(JsonStr);
        JSONArray JsonArr = JsonObj.getJSONArray("downloads");
        JsonArr.remove(JsonArr.size() - position - 1);
        Stream.write(JsonObj.toString(), file);
    }
}
