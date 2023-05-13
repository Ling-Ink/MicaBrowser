package com.moling.micabrowser.data;

import com.moling.micabrowser.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    public static Map<String, Object[]> Main() {
        Map<String, Object[]> map = new HashMap<>();
        map.put("title", new String[] { "搜索引擎", "App Center 使用报告" });
        map.put("type", new String[] { Constants.SETTING_TYPE_NEXT, Constants.SETTING_TYPE_SWITCH });
        map.put("key", new String[] { "SearchEngine", "UsageReport" });
        return map;
    }

    public static Map<String, Object[]> SearchEngine() {
        Map<String, Object[]> map = new HashMap<>();
        map.put("title", new String[] { "必应", "百度" });
        map.put("type", new String[] { Constants.SETTING_TYPE_RADIO, Constants.SETTING_TYPE_RADIO });
        map.put("key", new String[] { "bing", "baidu" });
        return map;
    }
}
