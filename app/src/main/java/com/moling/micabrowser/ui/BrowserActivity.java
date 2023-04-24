package com.moling.micabrowser.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moling.micabrowser.R;
import com.moling.micabrowser.databinding.ActivityBrowserBinding;
import com.moling.micabrowser.browser.History;
import com.moling.micabrowser.views.CircularProgressView;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

public class BrowserActivity extends XWalkActivity {
    private ActivityBrowserBinding binding;
    private Button mButtonMenu;
    private XWalkView mXWalkView;
    private CircularProgressView mProgressLoading;

    @Override
    protected void onXWalkReady() {
        XWalkSettings settings = mXWalkView.getSettings();
        // XWalkView 设置
        settings.setInitialPageScale(100); // 设置缩放比例
        settings.setJavaScriptEnabled(true); // 启用 JavaScript 支持
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 启用 JS 跳转
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        // XWalkView 事件监听
        mXWalkView.setResourceClient(new XWalkResourceClient(this.mXWalkView) {
            @Override
            public void onProgressChanged(XWalkView view, int i) {
                Log.d("[Progress]", String.valueOf(i));
                mProgressLoading.setProgress(i);
            }
            @Override
            public void onLoadFinished(XWalkView view, String url) {
                History.put(mXWalkView.getTitle(), mXWalkView.getUrl());
                Log.d("[LoadFinished]", mXWalkView.getTitle() + " | " + mXWalkView.getUrl());
                mProgressLoading.setProgress(0);
            }
        });
        // 加载 Url
        mXWalkView.loadUrl(getIntent().getDataString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBrowserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 控件绑定
        mXWalkView = binding.xwalkview;
        mButtonMenu = binding.buttonMenu;
        mProgressLoading = binding.progressLoading;

        // BottomSheet 初始化
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_menu_browser);

        // BottomSheet 按钮
        mButtonMenu.setOnClickListener(view -> {
            dialog.show();
        });

        // 后退按钮事件
        dialog.findViewById(R.id.button_back).setOnClickListener(view -> {
            if (mXWalkView.getNavigationHistory().canGoBack()) {
                mXWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
            } else {
                Toast.makeText(this,"已是最前", Toast.LENGTH_SHORT).show();
            }
        });

        // 刷新按钮事件
        dialog.findViewById(R.id.button_refresh).setOnClickListener(view -> {
            mXWalkView.reload(0);
        });

        // 前进按钮事件
        dialog.findViewById(R.id.button_forward).setOnClickListener(view -> {
            if (mXWalkView.getNavigationHistory().canGoForward()) {
                mXWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.FORWARD, 1);
            } else {
                Toast.makeText(this,"已是最后", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
