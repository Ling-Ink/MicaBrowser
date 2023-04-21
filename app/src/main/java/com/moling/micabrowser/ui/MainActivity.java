package com.moling.micabrowser.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moling.micabrowser.databinding.ActivityMainBinding;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.utils.Search;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private Button mButton_Menu;
    private EditText mEdit_Search;
    private FrameLayout mBottomSheetDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取SharedPreferences对象
        Global.sharedPreferences = getSharedPreferences("mica_browser_settings",MODE_PRIVATE);

        // 控件绑定
        mButton_Menu = binding.buttonMenu;
        mEdit_Search = binding.editSearch;
        mBottomSheetDialogue = binding.menu;

        // BottomSheet 初始化
        BottomSheetBehavior behavior = BottomSheetBehavior.from(mBottomSheetDialogue);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // BottomSheet 按钮
        mButton_Menu.setOnClickListener(view -> {
            if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // 搜索框回车事件
        mEdit_Search.setOnKeyListener((view, KeyCode, keyEvent) -> {
            if (KeyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN && !mEdit_Search.getText().toString().equals("")) {
                String url = Search.search(mEdit_Search.getText().toString());
                Log.d("[MainActivity]","Search - " + url);
            }
            return false;
        });
    }
}