package com.moling.micabrowser.ui;

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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moling.micabrowser.R;
import com.moling.micabrowser.databinding.ActivityMainBinding;
import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.widgets.URL.URLWidget;
import com.moling.micabrowser.browser.Search;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private Button mButtonMenu;
    private EditText mEditSearch;
    public static Handler search;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        Global.history = new URLWidget(new File(
                getFilesDir().getAbsolutePath() + File.separator + "mica_browser_history.json"
        ));
        Global.bookmark = new URLWidget(new File(
                getFilesDir().getAbsolutePath() + File.separator + "mica_browser_bookmarks.json"
        ));

        // 控件绑定
        mButtonMenu = binding.buttonMenu;
        mEditSearch = binding.editSearch;

        // BottomSheet 初始化
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_menu_main);

        // BottomSheet 按钮
        mButtonMenu.setOnClickListener(view -> {
            dialog.show();
        });

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
                Intent browserIntent = new Intent(MainActivity.this, BrowserActivity.class);
                browserIntent.setData(Uri.parse(url));
                startActivity(browserIntent);
            }
        };

        // 历史菜单
        dialog.findViewById(R.id.menu_history).setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_HISTORY));
            startActivity(menuIntent);
        });

        // 书签菜单
        dialog.findViewById(R.id.menu_bookmark).setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_BOOKMARK));
            startActivity(menuIntent);
        });
    }
}