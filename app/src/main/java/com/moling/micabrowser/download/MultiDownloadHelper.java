package com.moling.micabrowser.download;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.moling.micabrowser.ui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.EnumMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 参考：Android入门第56天-在Android里使用OKHttp多线程下载文件并展示其进度_android okhttp 下载_TGITCIC的博客-CSDN博客
 * https://blog.csdn.net/lifetragedy/article/details/128520249
 */

public class MultiDownloadHelper {
    private static final String TAG = "[Mica]";
    private int threadCount;
    private String downloadFilePath;
    private String downloadFileName;

    public MultiDownloadHelper(int threadCount, String filePath, String fileName) {
        this.threadCount = threadCount;
        this.downloadFilePath = filePath;
        this.downloadFileName = fileName;
    }

    private enum DownLoadThreadInfo {
        threadLength, startPosition
    }

    private EnumMap<DownLoadThreadInfo, Object> calcStartPosition(long fileLength, int threadNo) {
        int threadLength = (int) fileLength % threadCount == 0 ? (int) fileLength / threadCount : (int) fileLength + 1;
        int startPosition = threadNo * threadLength;
        EnumMap<DownLoadThreadInfo, Object> downloadThreadInfo = new EnumMap<DownLoadThreadInfo, Object>(DownLoadThreadInfo.class);
        downloadThreadInfo.put(DownLoadThreadInfo.threadLength, threadLength);
        downloadThreadInfo.put(DownLoadThreadInfo.startPosition, startPosition);
        return downloadThreadInfo;
    }

    private String generateTempFile(long fileLength) throws Exception {
        RandomAccessFile file = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String fileName = Environment.getExternalStoragePublicDirectory(DOWNLOAD_SERVICE).getCanonicalPath() + "/" + downloadFileName;
                Log.d(TAG,">>>>>>写入->"+fileName);
                file = new RandomAccessFile(fileName, "rwd");
                file.setLength(fileLength);
                return fileName;
            } else {
                throw new Exception("SD卡不可读写");
            }
        } catch (Exception e) {
            throw new Exception("GenerateTempFile error: " + e.getMessage(), e);
        } finally {
            try {
                file.close();
            } catch (Exception e) {
            }
        }

    }

    private class DownLoadThread extends Thread {
        private int threadId;
        private int startPosition;
        private RandomAccessFile threadFile;
        private int threadLength;
        private String downloadFilePath;
        private DownloadListener downloadListener;
        private int totalSize = 0;
        public DownLoadThread(int threadId, int startPosition,
                              RandomAccessFile threadFile, int threadLength, String downloadFilePath,DownloadListener downloadListener,
                              int totalSize) {
            this.threadId = threadId;
            this.startPosition = startPosition;
            this.threadFile = threadFile;
            this.threadLength = threadLength;
            this.downloadFilePath = downloadFilePath;
            this.downloadListener=downloadListener;
            this.totalSize=totalSize;
        }

        public void run() {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(downloadFilePath)//请求接口，如果需要传参拼接到接口后面
                    .build(); //创建Request对象
            Log.d(TAG, ">>>>>>线程" + (threadId + 1) + "开始下载...");
            Call call = client.newCall(request);
            //异步请求
            call.enqueue(new Callback() {
                //失败的请求
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, ">>>>>>下载进程加载->" + downloadFilePath + " error:" + e.getMessage(), e);
                }

                //结束的回调
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    Log.d(TAG, ">>>>>>连接->" + downloadFilePath + " 己经连接，进入下载...");
                    InputStream is = null;
                    try {
                        if (200 == response.code()) {
                            Log.d(TAG, ">>>>>>response.code()==" + response.code());
                            Log.d(TAG, ">>>>>>response.message()==" + response.message());
                            is = response.body().byteStream();
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            int length = 0;
                            while (length < threadLength && (len = is.read(buffer)) != -1) {
                                threadFile.write(buffer, 0, len);
                                //计算累计下载的长度
                                length += len;
                                downloadListener.onDownload(length,totalSize);
                            }
                            Log.d(TAG, ">>>>>>线程" + (threadId + 1) + "已下载完成");
                            Message downloadFinish = new Message();
                            downloadFinish.obj = downloadFileName + "\n下载完成";
                            MainActivity.toast.sendMessage(downloadFinish);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, ">>>>>>线程:" + threadId + " 下载出错: " + e.getMessage(), e);
                    } finally {
                        try {
                            threadFile.close();
                        } catch (Exception e) {
                        }
                        try {
                            is.close();
                            ;
                        } catch (Exception e) {
                        }
                    }

                }
            });

        }
    }

    public void download(DownloadListener downloadListener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadFilePath)//请求接口，如果需要传参拼接到接口后面
                .build(); //创建Request对象
        try {
            Call call = client.newCall(request);
            //异步请求
            call.enqueue(new Callback() {
                //失败的请求
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, ">>>>>>加载->" + downloadFilePath + " error:" + e.getMessage(), e);
                }

                //结束的回调
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    //响应码可能是404也可能是200都会走这个方法
                    Log.i(TAG, ">>>>>>the response code is: " + response.code());
                    if (200 == response.code()) {
                        Log.d(TAG, ">>>>>>response.code()==" + response.code());
                        Log.d(TAG, ">>>>>>response.message()==" + response.message());
                        try {
                            long size = response.body().contentLength();
                            Log.d(TAG, ">>>>>>file length->" + size);
                            for (int i = 0; i < threadCount; i++) {
                                EnumMap<DownLoadThreadInfo, Object> downLoadThreadInfoObjectEnumMap;
                                downLoadThreadInfoObjectEnumMap = calcStartPosition(size, i);
                                String threadFileName = generateTempFile(size);
                                int startPosition = (int) downLoadThreadInfoObjectEnumMap.get(DownLoadThreadInfo.startPosition);
                                int threadLength = (int) downLoadThreadInfoObjectEnumMap.get(DownLoadThreadInfo.threadLength);
                                RandomAccessFile threadFile = new RandomAccessFile(threadFileName, "rwd");
                                threadFile.seek(startPosition);
                                new DownLoadThread(i, startPosition, threadFile, threadLength, downloadFilePath,downloadListener,(int)size).start();
                                Log.d(TAG, ">>>>>>start thread: " + i + 1 + " start position->" + startPosition);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, ">>>>>>get remote file size error: " + e.getMessage(), e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, ">>>>>>open connection to path->" + downloadFilePath + "\nerror: " + e.getMessage(), e);
        }
    }
}
