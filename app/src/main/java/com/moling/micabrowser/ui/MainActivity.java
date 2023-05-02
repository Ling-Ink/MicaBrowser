package com.moling.micabrowser.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.moling.micabrowser.databinding.ActivityMainBinding;
import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.data.Data;
import com.moling.micabrowser.browser.Search;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    public static MainActivity mainActivity;
    private ActivityMainBinding binding;
    private Button mButtonHistory;
    private Button mButtonBookmark;
    private Button mButtonDownload;
    private EditText mEditSearch;
    public static Handler search;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainActivity = this;
        // Microsoft AppCenter Analytics
        Analytics.enableManualSessionTracker();
        AppCenter.start(getApplication(), "b0ecf30c-9f42-4d12-81ea-8f42a5e20158",
                Analytics.class, Crashes.class);
        Analytics.startSession();

        // 从 assets 中读取域名后缀列表
        try (InputStream input = getAssets().open("tlds-alpha-by-domain.txt")) {
            int n;
            StringBuilder sb = new StringBuilder();
            while ((n = input.read()) != -1) {
                sb.append((char) n);
            }
            Global.suffixList = sb.toString().split("\r\n");
        } catch (IOException e) {}
        // 获取SharedPreferences对象
        Global.sharedPreferences = getSharedPreferences("mica_browser_settings",MODE_PRIVATE);
        // 获取本地数据对象
        Global.data = new Data(getFilesDir().getAbsolutePath());

        // 控件绑定
        mEditSearch = binding.editSearch;
        mButtonHistory = binding.menuHistory;
        mButtonBookmark = binding.menuBookmark;
        mButtonDownload = binding.menuDownload;

        // 权限请求
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 222);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 222);

        // 搜索框回车事件
        mEditSearch.setOnKeyListener((view, KeyCode, keyEvent) -> {
            if (KeyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN && !mEditSearch.getText().toString().equals("")) {
                Message searchMsg = new Message();
                searchMsg.obj = mEditSearch.getText().toString();
                search.sendMessage(searchMsg);
            }
            return false;
        });

        // 搜索 Handler
        search = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String url = Search.search((String) msg.obj);
                Log.d("[Mica]","<MainActivity> | Search - " + url);
                try {
                    BrowserActivity.browserActivity.finish();
                } catch (Exception e) { }
                Intent browserIntent = new Intent(MainActivity.this, BrowserActivity.class);
                browserIntent.setData(Uri.parse(url));
                startActivity(browserIntent);
            }
        };

        // 历史菜单
        mButtonHistory.setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_HISTORY));
            startActivity(menuIntent);
        });

        // 书签菜单
        mButtonBookmark.setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_BOOKMARK));
            startActivity(menuIntent);
        });

        // 下载菜单
        mButtonDownload.setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_DOWNLOAD));
            startActivity(menuIntent);
        });
    }
}