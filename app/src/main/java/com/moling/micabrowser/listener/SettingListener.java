package com.moling.micabrowser.listener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;

import com.moling.micabrowser.R;
import com.moling.micabrowser.adapters.SettingAdapter;
import com.moling.micabrowser.ui.MainActivity;
import com.moling.micabrowser.ui.MenuActivity;
import com.moling.micabrowser.utils.Config;
import com.moling.micabrowser.utils.Constants;
import com.moling.micabrowser.utils.Global;

import java.util.Objects;

public class SettingListener {

    public static AdapterView.OnItemClickListener SettingClickListener(SettingAdapter adapter) {
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<SettingClickListener> | Setting item clicked");
            switch (Objects.requireNonNull(adapter.getItem(position).get("type"))) {
                case Constants.SETTING_TYPE_NEXT:
                    Intent menuIntent = new Intent(MainActivity.mainActivity, MenuActivity.class);
                    menuIntent.setData(Uri.parse(adapter.getItem(position).get("key")));
                    MenuActivity.menuActivity.finish();
                    MainActivity.mainActivity.startActivity(menuIntent);
                    break;
            }
        };
        return listener;
    }

    public static AdapterView.OnItemClickListener SearchEngineClickListener() {
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            Log.d("[Mica]", "<SearchEngineClickListener> | SearchEngine item clicked");

            Message adapterMsg = new Message();
            String[] settingTitle = new String[] { "必应", "百度" };
            String[] settingType = new String[] { Constants.SETTING_TYPE_RADIO, Constants.SETTING_TYPE_RADIO};
            String[] settingKey = new String[] { "bing", "baidu" };
            Object[] settingParam = new Object[]{ false, false };
            settingParam[position] = true;

            adapterMsg.obj = new SettingAdapter(MainActivity.mainActivity.getLayoutInflater(), settingTitle, settingType, settingKey, settingParam);
            MenuActivity.setSettingAdapter.sendMessage(adapterMsg);

            Config.setSearchEngine(Global.sharedPreferences, settingKey[position]);
        };
        return listener;
    }
}
