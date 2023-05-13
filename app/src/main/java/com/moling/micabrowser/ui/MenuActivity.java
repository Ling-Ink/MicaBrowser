package com.moling.micabrowser.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.moling.micabrowser.R;
import com.moling.micabrowser.adapters.SettingAdapter;
import com.moling.micabrowser.adapters.URLAdapter;
import com.moling.micabrowser.adapters.MenuAdapter;
import com.moling.micabrowser.listener.MenuListener;
import com.moling.micabrowser.data.models.DownloadModel;
import com.moling.micabrowser.databinding.ActivityMenuBinding;
import com.moling.micabrowser.listener.SettingListener;
import com.moling.micabrowser.utils.Config;
import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {
    public static MenuActivity menuActivity;
    private ActivityMenuBinding binding;

    private TextView mTextMenuTitle;
    private ListView mListMenu;
    private String menuType;

    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    public static Handler setURLAdapter;
    public static Handler setDownloadAdapter;
    public static Handler setSettingAdapter;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        menuActivity = this;

        String[] settingTitle;
        String[] settingType;
        String[] settingKey;
        Object[] settingParam;

        // 控件绑定
        mTextMenuTitle = binding.textMenuTitle;
        mListMenu = binding.listMenu;

        // 菜单类型
        menuType = getIntent().getData().toString();

        // URLAdapter Handler
        setURLAdapter = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                mListMenu.setAdapter((URLAdapter) msg.obj);
            }
        };
        // DownloadAdapter Handler
        setDownloadAdapter = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                mListMenu.setAdapter((ArrayAdapter<String>) msg.obj);
            }
        };
        // SettingAdapter Handler
        setSettingAdapter = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                mListMenu.setAdapter((SettingAdapter) msg.obj);
            }
        };

        // 设置 Listener 以及 Adapter
        Message adapterMsg = new Message();
        Object adapter;
        switch (menuType) {
            case Constants.MENU_TYPE_HISTORY:
                mTextMenuTitle.setText(getString(R.string.menu_history));

                adapter = new URLAdapter(getLayoutInflater(), MenuAdapter.HistoryAdapter());
                itemClickListener = MenuListener.HistoryClickListener((URLAdapter) adapter);
                itemLongClickListener = MenuListener.HistoryLongClickListener(getLayoutInflater());

                adapterMsg.obj = adapter;
                setURLAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_BOOKMARK:
                mTextMenuTitle.setText(getString(R.string.menu_bookmark));

                adapter = new URLAdapter(getLayoutInflater(), MenuAdapter.BookmarkAdapter());
                itemClickListener = MenuListener.BookmarkClickListener((URLAdapter) adapter);
                itemLongClickListener = MenuListener.BookmarkLongClickListener(getLayoutInflater());

                adapterMsg.obj = adapter;
                setURLAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_DOWNLOAD:
                mTextMenuTitle.setText(getString(R.string.menu_download));

                adapter = dumpDownloadsList(MenuAdapter.DownloadAdapter());
                itemClickListener = MenuListener.DownloadClickListener();
                itemLongClickListener = MenuListener.DownloadLongClickListener(getLayoutInflater());

                adapterMsg.obj = adapter;
                setDownloadAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_SETTING:
                mTextMenuTitle.setText(R.string.menu_setting);

                settingTitle = new String[] { "搜索引擎", "App Center 崩溃报告" };
                settingType = new String[] { Constants.SETTING_TYPE_NEXT, Constants.SETTING_TYPE_SWITCH};
                settingKey = new String[] { "SearchEngine", "CrashReport" };
                settingParam = new Object[]{ null, true };

                adapter = new SettingAdapter(getLayoutInflater(), settingTitle, settingType, settingKey, settingParam);
                itemClickListener = SettingListener.SettingClickListener((SettingAdapter) adapter);

                adapterMsg.obj = adapter;
                setSettingAdapter.sendMessage(adapterMsg);
                break;
            case "SearchEngine":
                mTextMenuTitle.setText("搜索引擎");

                settingTitle = new String[] { "必应", "百度" };
                settingType = new String[] { Constants.SETTING_TYPE_RADIO, Constants.SETTING_TYPE_RADIO};
                settingKey = new String[] { "bing", "baidu" };
                settingParam = new Object[]{ false, false };
                for (int i = 0; i < settingKey.length; i++) {
                    if (settingKey[i].equals(Config.getSearchEngine(Global.sharedPreferences))) {
                        settingParam[i] = true;
                    }
                }

                adapter = new SettingAdapter(getLayoutInflater(), settingTitle, settingType, settingKey, settingParam);
                itemClickListener = SettingListener.SearchEngineClickListener();

                adapterMsg.obj = adapter;
                setSettingAdapter.sendMessage(adapterMsg);
                break;
        }

        // 设置 listener
        if (itemClickListener != null) mListMenu.setOnItemClickListener((adapterView, view, i, l) -> itemClickListener.onItemClick(adapterView, view, i, l));
        if (itemLongClickListener != null) mListMenu.setOnItemLongClickListener((adapterView, view, i, l) -> itemLongClickListener.onItemLongClick(adapterView, view, i, l));
    }

    public static ArrayAdapter<String> dumpDownloadsList(List<DownloadModel> downloadModels) {
        List<String> downloads = new ArrayList<>();
        for (int i = 0; i < downloadModels.size(); i++) {
            downloads.add(downloadModels.get(i).getName());
        }
        return new ArrayAdapter<>(MenuActivity.menuActivity, android.R.layout.simple_list_item_1, downloads);
    }
}
