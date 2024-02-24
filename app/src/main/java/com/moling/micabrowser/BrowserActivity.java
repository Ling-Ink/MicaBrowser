package com.moling.micabrowser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moling.micabrowser.utils.circularprogressview.CircularProgressView;
import com.moling.micabrowser.databinding.ActivityBrowserBinding;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkDownloadListener;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

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
        mImageMenu.setOnClickListener(v -> showMenu());
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

    protected void showMenu() {
        Dialog mMenuDialog = new Dialog(this, R.style.BottomDialog);
        @SuppressLint("InflateParams") LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_menu, null);

        root.findViewById(R.id.button_close).setOnClickListener(v -> mMenuDialog.dismiss());

        mMenuDialog.setContentView(root);
        mMenuDialog.setCancelable(false);
        Window dialogWindow = mMenuDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //dialogWindow.setWindowAnimations(R.style.BottomDialog); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mMenuDialog.show();
    }
}
