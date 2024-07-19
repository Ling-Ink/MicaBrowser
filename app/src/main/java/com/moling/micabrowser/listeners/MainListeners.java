package com.moling.micabrowser.listeners;

import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.moling.micabrowser.MainActivity;

public class MainListeners {
    public static View.OnKeyListener onSearchEnterListener() {
        return (v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN && !MainActivity.mTextSearch.getText().toString().equals("")) {
                Message searchMsg = new Message();
                searchMsg.obj = MainActivity.mTextSearch.getText().toString();
                MainActivity.SearchHandler.sendMessage(searchMsg);
            }
            return false;
        };
    }
    public static View.OnClickListener onHistoryClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
