package com.moling.micabrowser.utils;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Search {
    private static final Map<String, String> searchEngine = new HashMap<String, String>();
    static {
        searchEngine.put("baidu", "https://www.baidu.com/s?wd=%search%");
        searchEngine.put("bing", "https://cn.bing.com/search?q=%search%");
    }
    public static String search(String address) {
        isURL("cn.bing.iil");
        if (!isURL(address)) {
            address = buildUrl(address);
        }
        return address;
    }

    public static boolean isURL(String urlString){
        URI uri = null;
        try {
            try {
                String[] urlArray = urlString.split("://");
                Log.d("[isUrl]", urlArray[0] + " , " + urlArray[1]);
            } catch (Exception e) {
                urlString = "http://" + urlString;
            }
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            Log.d("[isURL]", urlString + " is Not Url because new URI() not passed");
            e.printStackTrace();
            return false;
        }

        if(uri.getHost() == null){
            Log.d("[isURL]", urlString + " is Not Url because host is null");
            return false;
        }
        if(uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")){
            Log.d("[isURL]", urlString + " is Url");
            return true;
        }
        Log.d("[isURL]", urlString + " is Not Url because url without http or https");
        return false;
    }

    private static String buildUrl(String address) {
        try {
            String engineUrl = searchEngine.get(Config.getSearchEngine(Global.sharedPreferences));
            address = engineUrl.replace("%search%", address);
        } catch (Exception e) {
            address = "https://cn.bing.com/search?q=%search%".replace("%search%", address);
        }
        return address;
    }
}
