package com.moling.micabrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.moling.micabrowser.database.DAO.BookmarkDAO;
import com.moling.micabrowser.database.DAO.DownloadDAO;
import com.moling.micabrowser.database.DAO.HistoryDAO;
import com.moling.micabrowser.database.Database;
import com.moling.micabrowser.databinding.ActivityMainBinding;
import com.moling.micabrowser.datamigration.DataMigration;
import com.moling.micabrowser.listeners.MainListeners;
import com.moling.micabrowser.utils.ConfigUtils;
import com.moling.micabrowser.utils.DomainUtils;

public class MainActivity extends Activity {
    private ActivityMainBinding binding;

    @SuppressLint("StaticFieldLeak")
    public static EditText mTextSearch;
    private Button mButtonHistory;
    private Button mButtonBookmark;
    private Button mButtonDownload;
    private Button mButtonSetting;
    private Button mButtonAbout;

    public static Handler SearchHandler;

    public static BookmarkDAO bookmarkDAO;
    public static DownloadDAO downloadDAO;
    public static HistoryDAO historyDAO;
    private void BindWidgets() {
        mTextSearch = binding.editSearch;
        mButtonHistory = binding.menuHistory;
        mButtonBookmark = binding.menuBookmark;
        mButtonDownload = binding.menuDownload;
        mButtonSetting = binding.menuSetting;
        mButtonAbout = binding.menuAbout;
    }

    @SuppressLint("HandlerLeak")
    private void InitializeHandlers() {
        SearchHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                try { BrowserActivity.browserActivity.finish(); } catch (Exception ignored) { }
                String targetURL = DomainUtils.buildURL(getApplicationContext(), (String) msg.obj);
                Log.d("[Mica]", "Target URL: " + targetURL);
                startActivity(
                        new Intent(MainActivity.this, BrowserActivity.class)
                                .setData(Uri.parse(targetURL))
                );
            }
        };
    }

    private void InitializeDatabase() {
        Database database = Room.databaseBuilder(getApplicationContext(), Database.class, "MicaBrowserDB").build();
        bookmarkDAO = database.bookmarkDAO();
        downloadDAO = database.downloadDAO();
        historyDAO = database.historyDAO();
    }

    private void MigrateOldData() {
        DataMigration.SettingsMigration(getApplicationContext());
        DataMigration.BookmarksMigration(getApplicationContext());
        DataMigration.HistoriesMigration(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Microsoft AppCenter Analytics
        Analytics.enableManualSessionTracker();
        AppCenter.start(getApplication(), "b0ecf30c-9f42-4d12-81ea-8f42a5e20158",
                Analytics.class, Crashes.class);
        Analytics.startSession();

        BindWidgets();
        InitializeHandlers();
        InitializeDatabase();

        MigrateOldData();

        mTextSearch.setOnKeyListener(MainListeners.onSearchEnterListener());
    }

    public static void TrackUsage(Context appContext, Context baseContent) {
        try {
            // 获取程序包信息
            PackageInfo packageInfo = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
            // 获取程序信息
            ApplicationInfo applicationInfo = baseContent.getApplicationInfo();
            // 是否为 Debug 版本
            if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                Analytics.trackEvent("Browsed Once With Version { " + packageInfo.versionName + " [Debug] }");
            } else {
                Analytics.trackEvent("Browsed Once With Version { " + packageInfo.versionName + " }");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}