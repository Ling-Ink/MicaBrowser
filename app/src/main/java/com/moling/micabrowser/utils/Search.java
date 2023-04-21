package com.moling.micabrowser.utils;

import java.util.HashMap;
import java.util.Map;

public class Search {
    private static final Map<String, String> searchEngine = new HashMap<String, String>();
    static {
        searchEngine.put("baidu", "https://www.baidu.com/s?wd=%search%");
        searchEngine.put("bing", "https://cn.bing.com/search?q=%search%");
    }
    public static String search(String address) {
        try{
            String[] arrAddress = address.split("://");
            if (!(arrAddress[0] == "http" || arrAddress[0] == "https")) {
                address = buildUrl(address);
            }
        } catch (Exception e) {
            address = buildUrl(address);
        }
        return address;
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
