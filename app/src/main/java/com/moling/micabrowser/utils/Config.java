package com.moling.micabrowser.utils;

import android.content.SharedPreferences;

public class Config {
    public static String getSearchEngine(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("SearchEngine","");
    }
    public static void setSearchEngine(SharedPreferences sharedPreferences, String searchEngine) {
        sharedPreferences.edit().putString("SearchEngine", searchEngine).apply();
    }
}
