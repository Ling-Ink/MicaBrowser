package com.moling.micabrowser.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.moling.micabrowser.widgets.URL.URLAdapter;
import com.moling.micabrowser.browser.menu.Menu_Adapter;
import com.moling.micabrowser.browser.menu.Menu_Listener;
import com.moling.micabrowser.databinding.ActivityMenuBinding;
import com.moling.micabrowser.utils.Constants;

public class MenuActivity extends Activity {
    public static MenuActivity menuActivity;

    private ActivityMenuBinding binding;
    private ListView mListMenu;
    private String menuType;

    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;
    private URLAdapter adapter;
    public static Handler setAdapter;
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

        switch (menuType) {
            case Constants.MENU_TYPE_HISTORY:
                adapter = new URLAdapter(getLayoutInflater(), Menu_Adapter.HistoryAdapter());
                itemClickListener = Menu_Listener.HistoryClickListener(adapter);
                itemLongClickListener = Menu_Listener.HistoryLongClickListener(getLayoutInflater());
                break;
            case Constants.MENU_TYPE_BOOKMARK:
                adapter = new URLAdapter(getLayoutInflater(), Menu_Adapter.BookmarkAdapter());
                itemClickListener = Menu_Listener.BookmarkClickListener(adapter);
                itemLongClickListener = Menu_Listener.BookmarkLongClickListener(getLayoutInflater());
                break;
        }

        // 设置 adapter
        setAdapter = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                mListMenu.setAdapter((URLAdapter) msg.obj);
            }
        };
        Message adapterMsg = new Message();
        adapterMsg.obj = adapter;
        setAdapter.sendMessage(adapterMsg);
        // 设置 listener
        mListMenu.setOnItemClickListener((adapterView, view, i, l) -> itemClickListener.onItemClick(adapterView, view, i, l));
        mListMenu.setOnItemLongClickListener((adapterView, view, i, l) -> itemLongClickListener.onItemLongClick(adapterView, view, i, l));
    }
}
