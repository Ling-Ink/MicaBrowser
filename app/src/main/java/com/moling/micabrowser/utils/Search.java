package com.moling.micabrowser.utils;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Search {
    private static final Map<String, String> searchEngine = new HashMap<>();
    static {
        searchEngine.put("baidu", "https://www.baidu.com/s?wd=%search%");
        searchEngine.put("bing", "https://cn.bing.com/search?q=%search%");
    }
    public static String search(String address) {
        Log.d("[Mica]", "<search> | Searching address:[" + address + "]");
        try {
            // 判断是否存在已定义的 http/https 协议
            String[] urlArray = address.split("://");
            Log.d("[Mica]", "<search> | Url had scheme:[" + urlArray[0] + "], with:[" + urlArray[1] + "]");
        } catch (Exception e) {
            Log.d("[Mica]", "<search> | Url doesn't have scheme");
            if (isURL("http://" + address)) {
                // 添加协议后为合法 URL
                address = "http://" + address;
            } else if (isCorrectIp(address)) {
                address = "http://" + address;
            } else {
                // 非法 URL, 构建搜索 URL
                address = buildUrl(address);
            }
        }
        return address;
    }

    public static boolean isURL(String urlString){
        // 判断是否能转为 URI 类型
        URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            Log.d("[Mica]", "<isUrl> | " + urlString + " is Not Url because new URI() not passed");
            return false;
        }

        String host = uri.getHost();
        Log.d("[Mica]", "<isUrl> | Host is:[" + host + "]");
        // 判断 host 是否存在
        if(host == null){
            Log.d("[Mica]", "<isUrl> | " + urlString + " is Not Url because host is null");
            return false;
        }
        // 判断域名后缀是否合法
        try {
            String[] hostArray = host.split("\\.");
            String hostSuffix = hostArray[hostArray.length - 1];
            Log.d("[Mica]", "<isUrl> | Suffix is:[" + hostSuffix + "]");
            boolean isValid = false;
            for (int i = 1; i < Global.suffixList.length; i++) {
                if (Objects.equals(hostSuffix.toUpperCase(), Global.suffixList[i])) {
                    isValid = true;
                }
            }
            if (!isValid) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            Log.d("[Mica]", "<isUrl> | " + urlString + " is Not Url because url host doesn't have legal suffix");
            return false;
        }

        // 判断协议是否为 http/https
        Log.d("[Mica]", "<isUrl> | Scheme is:[" + uri.getScheme() + "]");
        if(uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")){
            Log.d("[Mica]", "<isUrl> | " + urlString + " is Url");
            return true;
        }
        Log.d("[Mica]", "<isUrl> | " + urlString + " is Not Url because url without http or https");
        return false;
    }

    //判断字符是否是IP
    public static boolean isCorrectIp(String ipString) {
        //1、判断是否是7-15位之间（0.0.0.0-255.255.255.255.255）
        if (ipString.length()<7||ipString.length()>15) {
            return false;
        }
        //2、判断是否能以小数点分成四段
        String[] ipArray = ipString.split("\\.");
        if (ipArray.length != 4) {
            return false;
        }
        for (int i = 0; i < ipArray.length; i++) {
            //3、判断每段是否都是数字
            try {
                int number = Integer.parseInt(ipArray[i]);
                //4.判断每段数字是否都在0-255之间
                if (number <0||number>255) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    // 构建搜索 URL
    private static String buildUrl(String address) {
        try {
            String engineUrl = searchEngine.get(Config.getSearchEngine());
            address = engineUrl.replace("%search%", address);
        } catch (Exception e) {
            address = "https://cn.bing.com/search?q=%search%".replace("%search%", address);
        }
        return address;
    }
}
