package com.moling.micabrowser.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moling.micabrowser.R;
import com.moling.micabrowser.databinding.ActivityMainBinding;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.utils.Search;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private Button mButtonMenu;
    private EditText mEditSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取SharedPreferences对象
        Global.sharedPreferences = getSharedPreferences("mica_browser_settings",MODE_PRIVATE);

        // 控件绑定
        mButtonMenu = binding.buttonMenu;
        mEditSearch = binding.editSearch;
        //mLayoutMenu = binding.layoutMenu;

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
                String url = Search.search(mEditSearch.getText().toString());
                Log.d("[MainActivity]","Search - " + url);
                Intent browserIntent = new Intent(this, BrowserActivity.class);
                browserIntent.setData(Uri.parse(url));
                startActivity(browserIntent);
            }
            return false;
        });
    }
}