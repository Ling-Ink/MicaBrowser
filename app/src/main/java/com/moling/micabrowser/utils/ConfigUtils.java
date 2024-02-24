package com.moling.micabrowser.utils;

import static com.moling.micabrowser.utils.ProgramUtils.getAppVersion;

import android.content.Context;

public class ConfigUtils {
    public static String getSearchEngine(Context context) {
        return context.getSharedPreferences("mica_browser_settings", Context.MODE_PRIVATE).getString("SearchEngine","https://cn.bing.com/search?q=%search%");
    }
    public static void setSearchEngine(Context context, String searchEngine) {
        context.getSharedPreferences("mica_browser_settings", Context.MODE_PRIVATE).edit().putString("SearchEngine", searchEngine).apply();
    }

    public static boolean getUsageReport(Context context) {
        return context.getSharedPreferences("mica_browser_settings", Context.MODE_PRIVATE).getBoolean("UsageReport", true);
    }
    public static void setUsageReport(Context context, boolean UsageReport) {
        context.getSharedPreferences("mica_browser_settings", Context.MODE_PRIVATE).edit().putBoolean("UsageReport", UsageReport).apply();
    }

    public static boolean isVersionChanged(Context context) {
        return context.getSharedPreferences("mica_browser_settings", Context.MODE_PRIVATE).getString("PreviousVersion", "").equals(getAppVersion());
    }
    public static void updateVersion(Context context) {
        context.getSharedPreferences("mica_browser_settings", Context.MODE_PRIVATE).edit().putString("PreviousVersion", getAppVersion()).apply();
    }
}
