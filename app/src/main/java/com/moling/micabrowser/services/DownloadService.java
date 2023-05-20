package com.moling.micabrowser.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

public class DownloadService extends Service {
    public static Handler DownloadHandler;

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     * @param intent
     */
    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @SuppressLint("HandlerLeak") @Override public void onCreate() {
        System.out.println("onCreate invoke");
        DownloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String downUrl = (String) msg.obj;
            }
        };
        super.onCreate();
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     */
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand invoke");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务销毁时的回调
     */
    @Override public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
    }
}
