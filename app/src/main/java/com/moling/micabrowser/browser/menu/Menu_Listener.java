package com.moling.micabrowser.browser.menu;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;

import com.moling.micabrowser.utils.Global;
import com.moling.micabrowser.widgets.URL.URLAdapter;
import com.moling.micabrowser.widgets.URL.URLWidget;
import com.moling.micabrowser.widgets.URL.URLModel;
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
            Menu_Listener_Utils.RemoveUrlByPosition(Global.history, inflater, position);
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
            Menu_Listener_Utils.RemoveUrlByPosition(Global.bookmark, inflater, position);
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
    protected static void RemoveUrlByPosition(URLWidget urlObj, LayoutInflater inflater, int position) {
        urlObj.delete(position);
        Message adapterMsg = new Message();
        adapterMsg.obj = new URLAdapter(inflater, urlObj.get());
        MenuActivity.setAdapter.sendMessage(adapterMsg);
    }
}
