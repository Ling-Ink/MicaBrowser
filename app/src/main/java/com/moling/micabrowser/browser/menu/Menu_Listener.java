package com.moling.micabrowser.browser.menu;

import android.os.Message;
import android.widget.AdapterView;

import com.moling.micabrowser.adapters.HistoryAdapter;
import com.moling.micabrowser.models.HistoryModel;
import com.moling.micabrowser.ui.MainActivity;
import com.moling.micabrowser.ui.MenuActivity;

public class Menu_Listener {
    public static AdapterView.OnItemClickListener HistoryListener(HistoryAdapter adapter){
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            HistoryModel model = (HistoryModel) adapter.getItem(position);
            Message searchMsg = new Message();
            searchMsg.obj = model.getUrl();
            MainActivity.search.sendMessage(searchMsg);
            MenuActivity.menuActivity.finish();
        };
        return listener;
    }
}
