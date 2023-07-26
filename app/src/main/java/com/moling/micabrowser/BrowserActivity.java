package com.moling.micabrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moling.micabrowser.circularprogressview.CircularProgressView;
import com.moling.micabrowser.databinding.ActivityBrowserBinding;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkDownloadListener;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class BrowserActivity extends XWalkActivity {
    @SuppressLint("StaticFieldLeak")
    public static BrowserActivity browserActivity;
    private ActivityBrowserBinding binding;

    private ImageView mImageMenu;
    private XWalkView mXWalkView;
    private CircularProgressView mProgressLoading;

    private void BindWidgets() {
        mXWalkView = binding.xwalkview;
        mImageMenu = binding.imageMenu;
        mProgressLoading = binding.progressLoading;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBrowserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        browserActivity = this;

        BindWidgets();
    }

    // XWalkView 准备完毕事件
    @SuppressLint("ClickableViewAccessibility")
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
                mProgressLoading.setProgress(i);
            }
            @Override
            public void onLoadStarted(XWalkView view, String url) {
                /* TODO */
            }
            @Override
            public void onLoadFinished(XWalkView view, String url) {
                MainActivity.TrackUsage(getApplicationContext(), getBaseContext());
                mProgressLoading.setProgress(0);
                /* TODO */
            }
        });
        mXWalkView.setDownloadListener(new XWalkDownloadListener(getApplicationContext()) {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                /* TODO */
            }
        });
        // 加载 Url
        String url = getIntent().getData().toString();
        Log.d("[Mica]", "<BrowserActivity> | LoadURL - " + url);
        mXWalkView.loadUrl(url);
    }
}
