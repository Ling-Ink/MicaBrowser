package com.moling.micabrowser.utils;

public class Config {
    public static String getSearchEngine() {
        return Global.sharedPreferences.getString("SearchEngine","");
    }
    public static void setSearchEngine(String searchEngine) {
        Global.sharedPreferences.edit().putString("SearchEngine", searchEngine).apply();
    }

    public static boolean getUsageReport() {
        return Global.sharedPreferences.getBoolean("UsageReport", true);
    }
    public static void setUsageReport(boolean UsageReport) {
        Global.sharedPreferences.edit().putBoolean("UsageReport", UsageReport).apply();
    }
}
