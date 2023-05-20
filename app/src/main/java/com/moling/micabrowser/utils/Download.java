package com.moling.micabrowser.utils;

import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 参考：java从网络Url中下载文件并保存到本地 - 简书
 * https://www.jianshu.com/p/a841623e0afa
 */

public class Download {
    /**
     * 从网络Url中下载文件
     *
     * @param urlStr 将要下载的 URL 字符串
     * @param fileName 保存文件名
     * @param savePath 保存路径
     * @param userAgent 下载 UA
     */
    public static String fromUrl(String urlStr, String fileName, String savePath, String userAgent) {
        try {
            Log.i("[Mica]", "Received Download Request:" + fileName);
            URL url = new URL(urlStr);
            String[] downloadInfo = getInfo(url, userAgent);
            // 是否存在可用的 Accept-Ranges 属性
            boolean accept_Ranges = Boolean.parseBoolean(downloadInfo[0]);
            // 文件大小
            long content_Length = Long.parseLong(downloadInfo[1]);
            // 文件保存位置
            File file = new File(savePath + File.separator + fileName);
            if (accept_Ranges) {
                Log.i("[Mica]", "Download By Block");
                downloadByBlock(url, userAgent, content_Length, file);
            } else {
                Log.i("[Mica]", "Download By Full");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", userAgent);

                // 得到输入流
                InputStream inputStream = conn.getInputStream();
                // 获取字节数组
                byte[] getData = readInputStream(inputStream);

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData);
                fos.close();
                inputStream.close();
            }
            Log.i("[Mica]", "Download Finished:" + fileName);
            return savePath + File.separator + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static void downloadByBlock(URL url, String userAgent, long contentLength, File saveFile) {
        try(FileOutputStream fos = new FileOutputStream(saveFile)) {
            long start = 0; long end = contentLength > 1024 ? 1023 : contentLength;
            while (contentLength > 0) {
                fos.write(getContentByRange(url, userAgent, start, end));
                //Log.i("[Mica]", "Downloading Range: " + start + "-" + end);
                start += 1024; end += 1024; contentLength -= 1024;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getContentByRange(URL url, String userAgent, long start, long end) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", userAgent);
            // 设置下载区间
            conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            // 获取字节数组
            return readInputStream(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取下载目标属性
     *
     * @param url 下载 URL 对象
     * @param userAgent 下载 UA
     * @return String[ Accept-Range, Content-Length ]
     */
    public static String[] getInfo(URL url, String userAgent) {
        boolean Accept_Ranges = false;
        int Content_Length;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 使用 HEAD 请求获取信息
            conn.setRequestMethod("HEAD");
            conn.setRequestProperty("User-Agent", userAgent);
            Map<String, List<String>> Headers = conn.getHeaderFields();
            if (Headers.containsKey("Accept-Ranges")) {
                Accept_Ranges = Objects.equals(Objects.requireNonNull(Headers.get("Accept-Ranges")).get(0).toLowerCase(), "bytes");
            }
            Content_Length = Integer.parseInt(Objects.requireNonNull(Headers.get("Content-Length")).get(0));
            return new String[]{String.valueOf(Accept_Ranges), String.valueOf(Content_Length)};
        } catch (IOException e) {
            return new String[]{String.valueOf(false), String.valueOf(0)};
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 对 URL 编码进行解码
     * @param str 待解码的 URL 编码字符串
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
