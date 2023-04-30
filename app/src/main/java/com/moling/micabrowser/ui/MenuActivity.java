package com.moling.micabrowser.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.moling.micabrowser.data.adapters.URLAdapter;
import com.moling.micabrowser.browser.menu.Menu_Adapter;
import com.moling.micabrowser.browser.menu.Menu_Listener;
import com.moling.micabrowser.data.models.DownloadModel;
import com.moling.micabrowser.databinding.ActivityMenuBinding;
import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {
    public static MenuActivity menuActivity;

    private ActivityMenuBinding binding;
    private ListView mListMenu;
    private String menuType;

    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;
    private Object adapter;
    public static Handler setURLAdapter;
    public static Handler setDownloadAdapter;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        menuActivity = this;

        // 控件绑定
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

        // 设置 Listener 以及 Adapter
        Message adapterMsg = new Message();
        switch (menuType) {
            case Constants.MENU_TYPE_HISTORY:
                adapter = (Object) new URLAdapter(getLayoutInflater(), Menu_Adapter.HistoryAdapter());
                itemClickListener = Menu_Listener.HistoryClickListener((URLAdapter) adapter);
                itemLongClickListener = Menu_Listener.HistoryLongClickListener(getLayoutInflater());

                adapterMsg.obj = adapter;
                setURLAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_BOOKMARK:
                adapter = (Object) new URLAdapter(getLayoutInflater(), Menu_Adapter.BookmarkAdapter());
                itemClickListener = Menu_Listener.BookmarkClickListener((URLAdapter) adapter);
                itemLongClickListener = Menu_Listener.BookmarkLongClickListener(getLayoutInflater());

                adapterMsg.obj = adapter;
                setURLAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_DOWNLOAD:
                adapter = (Object) dumpDownloadsList(Global.data.getDownload());
                itemLongClickListener = Menu_Listener.DownloadLongClickListener(getLayoutInflater());

                adapterMsg.obj = adapter;
                setDownloadAdapter.sendMessage(adapterMsg);
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
