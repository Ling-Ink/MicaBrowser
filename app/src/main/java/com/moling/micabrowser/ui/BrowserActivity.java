package com.moling.micabrowser.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.moling.micabrowser.R;
import com.moling.micabrowser.databinding.ActivityBrowserBinding;
import com.moling.micabrowser.download.DownloadListener;
import com.moling.micabrowser.download.MultiDownloadHelper;
import com.moling.micabrowser.services.DownloadService;
import com.moling.micabrowser.utils.Config;
import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Download;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.views.CircularProgressView;
import com.moling.micabrowser.data.models.URLModel;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkDownloadListener;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class BrowserActivity extends XWalkActivity {
    public static BrowserActivity browserActivity;
    // 页面控件
    private BottomSheetDialog dialog;
    private ActivityBrowserBinding binding;
    private ImageView mImageMenu;
    private XWalkView mXWalkView;
    private CircularProgressView mProgressLoading;
    // XWalkView 拖动相关
    private final int DEFAULT_VIEW_LOCATION = 100;
        // 移动方向 5:左上 9:右上 6:左下 10:右下
    private int MoveDestination = 0;
        // 响应拖动区域大小
    private int TouchAreaSize = 120;
        // 可拖动区域大小
    private int MoveAreaSize = 120;
        // 按下时 XWalkView 位置
    private int ViewLeft;
    private int ViewTop;
        // 按下位置
    private int StartX;
    private int StartY;

    // 监听 XWalkView 拖动事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 存储按下时 XWalkView 位置
            ViewLeft = mXWalkView.getLeft();
            ViewTop = mXWalkView.getTop();
            // 存储按下位置
            StartX = (int) event.getRawX();
            StartY = (int) event.getRawY();
            Log.i("[Mica]", "onTouch: 按下X:" + StartX + " Y:" + StartY + " ViewLeft:" + ViewLeft + " ViewTop:" + ViewTop);
            if (StartY < TouchAreaSize) {
                MoveDestination = MoveDestination + 1;
            } else if (StartY > getResources().getDisplayMetrics().heightPixels - TouchAreaSize) {
                MoveDestination = MoveDestination + 2;
            }
            if (StartX < TouchAreaSize) {
                MoveDestination = MoveDestination + 4;
            } else if (StartX > getResources().getDisplayMetrics().widthPixels - TouchAreaSize) {
                MoveDestination = MoveDestination + 8;
            }
            Log.i("[Mica]", "MoveDestination:" + MoveDestination);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            MoveDestination = 0;
            Log.i("[Mica]", "onTouch: 抬起");
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 处理接收到触摸事件并且 MoveDestination 被赋值时的操作
            int MoveX = (int) event.getRawX() - StartX;
            int MoveY = (int) event.getRawY() - StartY;
            // 移动后的位置
            int MoveToX = ViewLeft - DEFAULT_VIEW_LOCATION + MoveX;
            int MoveToY = ViewTop - DEFAULT_VIEW_LOCATION + MoveY;
            switch (MoveDestination) {
                case 5: // 左上向右下移动
                    if (MoveToX > MoveAreaSize) { // 移动超出最大范围
                        MoveX = MoveAreaSize - (ViewLeft - DEFAULT_VIEW_LOCATION);
                    } else if (MoveToX < 0) { // 移动超出最小范围
                        MoveX = DEFAULT_VIEW_LOCATION - ViewLeft;
                    }
                    if (MoveToY > MoveAreaSize) { // 移动超出最大范围
                        MoveY = MoveAreaSize - (ViewTop - DEFAULT_VIEW_LOCATION);
                    } else if (MoveToY < 0) { // 移动超出最小范围
                        MoveY = DEFAULT_VIEW_LOCATION - ViewTop;
                    }
                    break;
                case 9: // 右上向左下移动
                    if (MoveToX < -MoveAreaSize) { // 移动超出最大范围
                        MoveX = ViewLeft - DEFAULT_VIEW_LOCATION - MoveAreaSize;
                    } else if (MoveToX > 0) { // 移动超出最小范围
                        MoveX = DEFAULT_VIEW_LOCATION - ViewLeft;
                    }
                    if (MoveToY > MoveAreaSize) { // 移动超出最大范围
                        MoveY = MoveAreaSize - (ViewTop - DEFAULT_VIEW_LOCATION);
                    } else if (MoveToY < 0) { // 移动超出最小范围
                        MoveY = DEFAULT_VIEW_LOCATION - ViewTop;
                    }
                    break;
                case 6: // 左下向右上移动
                    if (MoveToX > MoveAreaSize) { // 移动超出最大范围
                        MoveX = MoveAreaSize - (ViewLeft - DEFAULT_VIEW_LOCATION);
                    } else if (MoveToX < 0) { // 移动超出最小范围
                        MoveX = DEFAULT_VIEW_LOCATION - ViewLeft;
                    }
                    if (MoveToY < -MoveAreaSize) { // 移动超出最大范围
                        MoveY = ViewTop - DEFAULT_VIEW_LOCATION - MoveAreaSize;
                    } else if (MoveToY > 0) { // 移动超出最小范围
                        MoveY = DEFAULT_VIEW_LOCATION - ViewTop;
                    }
                    break;
                case 10: // 右下向左上移动
                    if (MoveToX < -MoveAreaSize) { // 移动超出最大范围
                        MoveX = ViewLeft - DEFAULT_VIEW_LOCATION - MoveAreaSize;
                    } else if (MoveToX > 0) { // 移动超出最小范围
                        MoveX = DEFAULT_VIEW_LOCATION - ViewLeft;
                    }
                    if (MoveToY < -MoveAreaSize) { // 移动超出最大范围
                        MoveY = ViewTop - DEFAULT_VIEW_LOCATION - MoveAreaSize;
                    } else if (MoveToY > 0) { // 移动超出最小范围
                        MoveY = DEFAULT_VIEW_LOCATION - ViewTop;
                    }
                    break;
                default:
                    MoveX = 0;
                    MoveY = 0;
                    break;
            }
            if (MoveX != 0 && MoveY != 0) {
                Log.i("[Mica]", "onTouch: 移动X:" + MoveX + " Y:" + MoveY);
                mXWalkView.layout(ViewLeft + MoveX, ViewTop + MoveY, ViewLeft + mXWalkView.getWidth() + MoveX, ViewTop + mXWalkView.getHeight() + MoveY);
                // 在移动 XWalkView 时禁用右划返回
                return false;
            }
        }

        try{
            return super.dispatchTouchEvent(event);
        } catch(java.lang.RuntimeException e) {
            if ( Objects.requireNonNull(e.getMessage()).compareTo("Crosswalk's APIs are not ready yet") == 0 ) {
            } else {
                throw e;
            }
        }
        return onTouchEvent(event);
    }

    // onCreate 事件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBrowserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        browserActivity = this;

        // 控件绑定
        mXWalkView = binding.xwalkview;
        mImageMenu = binding.imageMenu;
        mProgressLoading = binding.progressLoading;
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
                Log.d("[Mica]", "<Progress> | " + i);
                mProgressLoading.setProgress(i);
            }
            @Override
            public void onLoadStarted(XWalkView view, String url) {
                Global.data.putHistory(mXWalkView.getTitle(), mXWalkView.getUrl());
                Log.d("[Mica]", "<LoadStarted> | " + mXWalkView.getTitle() + " - " + mXWalkView.getUrl());
            }
            @Override
            public void onLoadFinished(XWalkView view, String url) {
                Global.data.putHistory(mXWalkView.getTitle(), mXWalkView.getUrl());
                Log.d("[Mica]", "<LoadFinished> | " + mXWalkView.getTitle() + " - " + mXWalkView.getUrl());
                mProgressLoading.setProgress(0);
                // 初始化 URL 文本框
                ((EditText) dialog.findViewById(R.id.text_url)).setText(mXWalkView.getUrl());
                // 初始化标题文本框
                ((TextView) dialog.findViewById(R.id.dialog_text_title)).setText(mXWalkView.getTitle());
                // 浏览记录
                if (Config.getUsageReport()) {
                    try {
                        // 获取程序包信息
                        PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                        // 获取程序信息
                        ApplicationInfo applicationInfo = getBaseContext().getApplicationInfo();
                        Message trackMsg = new Message();
                        // 是否为 Debug 版本
                        if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                            trackMsg.obj = "Browsed Once With Version { " + packageInfo.versionName + " [Debug] }";
                        } else {
                            trackMsg.obj = "Browsed Once With Version { " + packageInfo.versionName + " }";
                        }
                        MainActivity.track.sendMessage(trackMsg);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mXWalkView.setDownloadListener(new XWalkDownloadListener(getApplicationContext()) {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String[] urlArray = url.split("/");
                String downloadedFileName = Download.getURLDecoderString(urlArray[urlArray.length - 1]).split("\\?")[0];
                Log.d("[Mica]", "url: " + url + " | UA: " + userAgent + " | contentDisposition: " + contentDisposition + " | contentLength: " + contentLength);
                Toast.makeText(MainActivity.mainActivity,downloadedFileName + "\n开始下载", Toast.LENGTH_SHORT).show();
                String downloadLoc = Environment.getExternalStoragePublicDirectory(DOWNLOAD_SERVICE).getAbsolutePath() + File.separator + urlArray[urlArray.length - 1];
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String downloadHash = String.valueOf((downloadedFileName + timestamp.getTime()).hashCode());
                // 写下载项目
                Global.data.putDownload(
                        downloadedFileName,
                        downloadLoc,
                        downloadHash,
                        0);
                // 下载
                Intent downloadService = new Intent(MainActivity.mainActivity, DownloadService.class);
                downloadService.setData(Uri.parse(url + "@" + downloadedFileName + "@" + downloadHash));
                startService(downloadService);
            }
        });
        // 加载 Url
        String url = getIntent().getData().toString();
        Log.d("[Mica]", "<BrowserActivity> | LoadURL - " + url);
        mXWalkView.loadUrl(url);

        // BottomSheet 初始化
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_menu);

        // BottomSheet 按钮
        mImageMenu.setOnClickListener(view -> {
            // 初始化书签按钮
            List<URLModel> bookmarksList = Global.data.getBookMark();
            for (int i = 0; i < bookmarksList.size(); i++) {
                if (Objects.equals(mXWalkView.getUrl(), bookmarksList.get(i).getUrl())) {
                    ((ImageButton) dialog.findViewById(R.id.button_bookmark)).setImageResource(R.drawable.bookmark_starred);
                    break;
                } else {
                    ((ImageButton) dialog.findViewById(R.id.button_bookmark)).setImageResource(R.drawable.bookmark);
                }
            }
            dialog.findViewById(R.id.dialog_scrollview).scrollTo(0, 0);
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

        // 书签按钮事件
        dialog.findViewById(R.id.button_bookmark).setOnClickListener(view -> {
            List<URLModel> bookmarksList = Global.data.getBookMark();
            // 书签已存在
            for (int i = 0; i < bookmarksList.size(); i++) {
                if (Objects.equals(mXWalkView.getUrl(), bookmarksList.get(i).getUrl())) {
                    Global.data.delBookMark(bookmarksList.size() - 1 - i);
                    ((ImageButton) dialog.findViewById(R.id.button_bookmark)).setImageResource(R.drawable.bookmark);
                    return;
                }
            }
            // 书签不存在
            Global.data.putBookMark(mXWalkView.getTitle(), mXWalkView.getUrl());
            ((ImageButton) dialog.findViewById(R.id.button_bookmark)).setImageResource(R.drawable.bookmark_starred);
        });

        // 前进按钮事件
        dialog.findViewById(R.id.button_forward).setOnClickListener(view -> {
            if (mXWalkView.getNavigationHistory().canGoForward()) {
                mXWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.FORWARD, 1);
            } else {
                Toast.makeText(this,"已是最后", Toast.LENGTH_SHORT).show();
            }
        });

        // URL 文本框回车事件
        EditText urlText = dialog.findViewById(R.id.text_url);
        urlText.setOnKeyListener((view, KeyCode, keyEvent) -> {
            if (KeyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN && !urlText.getText().toString().equals("")) {
                Message searchMsg = new Message();
                searchMsg.obj = urlText.getText().toString();
                MainActivity.search.sendMessage(searchMsg);
            }
            return false;
        });

        // 历史记录按钮事件
        dialog.findViewById(R.id.dialog_button_history).setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_HISTORY));
            startActivity(menuIntent);
        });

        // 书签按钮事件
        dialog.findViewById(R.id.dialog_button_bookmark).setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_BOOKMARK));
            startActivity(menuIntent);
        });

        // 下载按钮事件
        dialog.findViewById(R.id.dialog_button_download).setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.setData(Uri.parse(Constants.MENU_TYPE_DOWNLOAD));
            startActivity(menuIntent);
        });
    }
}
