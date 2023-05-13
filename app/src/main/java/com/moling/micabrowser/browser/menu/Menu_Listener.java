package com.moling.micabrowser.browser.menu;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.Toast;

import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.data.adapters.URLAdapter;
import com.moling.micabrowser.data.models.URLModel;
import com.moling.micabrowser.ui.MainActivity;
import com.moling.micabrowser.ui.MenuActivity;

public class Menu_Listener {
    public static AdapterView.OnItemClickListener HistoryClickListener(URLAdapter adapter){
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<HistoryClickListener> | History item clicked");
            Menu_Listener_Utils.AccessURLByPosition(adapter, position);
        };
        return listener;
    }
    public static AdapterView.OnItemLongClickListener HistoryLongClickListener(LayoutInflater inflater) {
        AdapterView.OnItemLongClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<HistoryClickListener> | History item long clicked:[" + position + "]");
            Menu_Listener_Utils.RemoveByPosition(Constants.MENU_TYPE_HISTORY, inflater, position);
            return true;
        };
        return listener;
    }

    public static AdapterView.OnItemClickListener BookmarkClickListener(URLAdapter adapter){
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<BookmarkClickListener> | Bookmark item clicked");
            Menu_Listener_Utils.AccessURLByPosition(adapter, position);
        };
        return listener;
    }
    public static AdapterView.OnItemLongClickListener BookmarkLongClickListener(LayoutInflater inflater) {
        AdapterView.OnItemLongClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<BookmarkLongClickListener> | Bookmark item long clicked:[" + position + "]");
            Menu_Listener_Utils.RemoveByPosition(Constants.MENU_TYPE_BOOKMARK, inflater, position);
            return true;
        };
        return listener;
    }

    public static AdapterView.OnItemClickListener DownloadClickListener(){
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<DownloadClickListener> | Download item clicked");
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) MainActivity.mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", Global.data.getDownload().get(position).getLocation());
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Toast.makeText(MainActivity.mainActivity, "已复制文件路径", Toast.LENGTH_SHORT).show();
        };
        return listener;
    }
    public static AdapterView.OnItemLongClickListener DownloadLongClickListener(LayoutInflater inflater) {
        AdapterView.OnItemLongClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<DownloadLongClickListener> | Download item long clicked:[" + position + "]");
            Menu_Listener_Utils.RemoveByPosition(Constants.MENU_TYPE_DOWNLOAD, inflater, position);
            return true;
        };
        return listener;
    }
}

class Menu_Listener_Utils {
    protected static void AccessURLByPosition(URLAdapter adapter, int position) {
        URLModel model = (URLModel) adapter.getItem(position);
        Message searchMsg = new Message();
        searchMsg.obj = model.getUrl();
        MainActivity.search.sendMessage(searchMsg);
        MenuActivity.menuActivity.finish();
    }
    protected static void RemoveByPosition(String type, LayoutInflater inflater, int position) {
        Message adapterMsg;
        switch (type) {
            case Constants.MENU_TYPE_HISTORY:
                Global.data.delHistory(position);
                adapterMsg = new Message();
                adapterMsg.obj = new URLAdapter(inflater, Global.data.getHistory());
                MenuActivity.setURLAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_BOOKMARK:
                Global.data.delBookMark(position);
                adapterMsg = new Message();
                adapterMsg.obj = new URLAdapter(inflater, Global.data.getBookMark());
                MenuActivity.setURLAdapter.sendMessage(adapterMsg);
                break;
            case Constants.MENU_TYPE_DOWNLOAD:
                Global.data.delDownload(position);
                adapterMsg = new Message();
                adapterMsg.obj = MenuActivity.dumpDownloadsList(Global.data.getDownload());
                MenuActivity.setDownloadAdapter.sendMessage(adapterMsg);
                break;
        }
    }
}
